package com.cyx.supervisorterminal.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cyx.exception.BusException;
import com.cyx.supervisorterminal.entity.dto.AlarmHandleDTO;
import com.cyx.supervisorterminal.entity.dto.AlarmCloseDTO;
import com.cyx.supervisorterminal.entity.po.BizAlarmRecord;
import com.cyx.supervisorterminal.entity.po.BizAlarmRule;
import com.cyx.supervisorterminal.entity.po.BizDevice;
import com.cyx.supervisorterminal.entity.po.BizDeviceMonitorData;
import com.cyx.supervisorterminal.entity.vo.AlarmRecordVO;
import com.cyx.supervisorterminal.mapper.BizAlarmRecordMapper;
import com.cyx.supervisorterminal.mapper.BizAlarmRuleMapper;
import com.cyx.supervisorterminal.mapper.BizProjectMapper;
import com.cyx.supervisorterminal.security.CurrentUserAccess;
import com.cyx.supervisorterminal.service.AlarmService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * 告警业务实现，负责阈值判断、告警去重、数据权限和处理留痕。
 */
@Service
public class AlarmServiceImpl implements AlarmService {
    private final BizAlarmRuleMapper ruleMapper;
    private final BizAlarmRecordMapper alarmMapper;
    private final BizProjectMapper projectMapper;
    private final CurrentUserAccess currentUserAccess;

    public AlarmServiceImpl(BizAlarmRuleMapper ruleMapper, BizAlarmRecordMapper alarmMapper,
                            BizProjectMapper projectMapper, CurrentUserAccess currentUserAccess) {
        this.ruleMapper = ruleMapper;
        this.alarmMapper = alarmMapper;
        this.projectMapper = projectMapper;
        this.currentUserAccess = currentUserAccess;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void evaluate(BizDevice device, BizDeviceMonitorData data) {
        List<BizAlarmRule> rules = ruleMapper.selectList(Wrappers.<BizAlarmRule>lambdaQuery()
                .eq(BizAlarmRule::getEnabled, 1)
                .and(query -> query.isNull(BizAlarmRule::getDeviceType)
                        .or().eq(BizAlarmRule::getDeviceType, device.getDeviceType())));
        for (BizAlarmRule rule : rules) {
            BigDecimal actual = metricValue(data, rule.getMetricName());
            if (actual == null || !matches(actual, rule.getThresholdValue(), rule.getComparisonOperator())) {
                continue;
            }
            long activeCount = alarmMapper.selectCount(Wrappers.<BizAlarmRecord>lambdaQuery()
                    .eq(BizAlarmRecord::getDeviceId, device.getId()).eq(BizAlarmRecord::getRuleId, rule.getId())
                    .in(BizAlarmRecord::getAlarmStatus, "PENDING", "HANDLING"));
            if (activeCount == 0) {
                alarmMapper.insert(buildAlarm(device, data, rule, actual));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AlarmRecordVO> list() {
        List<BizAlarmRecord> alarms = currentUserAccess.canAccessAllProjects()
                ? alarmMapper.selectList(Wrappers.<BizAlarmRecord>lambdaQuery()
                .orderByDesc(BizAlarmRecord::getTriggeredAt).orderByDesc(BizAlarmRecord::getId))
                : alarmMapper.selectByProjectMemberUsername(currentUserAccess.username());
        return alarms.stream().map(this::toVO).toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(Long id, AlarmHandleDTO dto) {
        BizAlarmRecord alarm = requireAccessibleAlarm(id);
        if ("RESOLVED".equals(alarm.getAlarmStatus()) || "CLOSED".equals(alarm.getAlarmStatus())) {
            throw new BusException("已解决或已关闭的告警不能再次处理");
        }
        alarm.setAlarmStatus(dto.isResolved() ? "RESOLVED" : "HANDLING");
        alarm.setHandlerId(currentUserAccess.userId());
        alarm.setHandledAt(LocalDateTime.now());
        alarm.setHandleRemark(dto.getHandleRemark());
        alarmMapper.updateById(alarm);
    }

    /**
     * 只有已经解决的告警才能关闭，防止跳过实际处理过程直接结束告警。
     */
    @Override
    public void close(Long id, AlarmCloseDTO dto) {
        BizAlarmRecord alarm = requireAccessibleAlarm(id);
        if (!"RESOLVED".equals(alarm.getAlarmStatus())) {
            throw new BusException("只有已解决的告警才能关闭");
        }
        alarm.setAlarmStatus("CLOSED");
        alarm.setHandlerId(currentUserAccess.userId());
        alarm.setHandledAt(LocalDateTime.now());
        alarm.setHandleRemark(dto.getCloseRemark());
        alarmMapper.updateById(alarm);
    }

    /**
     * 校验告警存在且属于当前用户的数据范围。
     */
    private BizAlarmRecord requireAccessibleAlarm(Long id) {
        BizAlarmRecord alarm = alarmMapper.selectById(id);
        if (alarm == null) throw new BusException("告警不存在");
        if (!currentUserAccess.canAccessAllProjects()
                && projectMapper.countAccessibleProject(alarm.getProjectId(), currentUserAccess.username()) == 0) {
            throw new BusException("无权访问该告警");
        }
        return alarm;
    }

    /**
     * 从环境采样中安全提取规则指定的指标。
     */
    private BigDecimal metricValue(BizDeviceMonitorData data, String metric) {
        return switch (metric) {
            case "pm25" -> data.getPm25();
            case "pm10" -> data.getPm10();
            case "noiseDb" -> data.getNoiseDb();
            case "temperature" -> data.getTemperature();
            case "humidity" -> data.getHumidity();
            case "windSpeed" -> data.getWindSpeed();
            default -> null;
        };
    }

    /**
     * 执行规则配置的数值比较。
     */
    private boolean matches(BigDecimal actual, BigDecimal threshold, String operator) {
        if (threshold == null) return false;
        int result = actual.compareTo(threshold);
        return switch (operator) {
            case "GT" -> result > 0;
            case "GTE" -> result >= 0;
            case "LT" -> result < 0;
            case "LTE" -> result <= 0;
            case "EQ" -> result == 0;
            default -> false;
        };
    }

    /**
     * 创建携带规则快照值的告警记录。
     */
    private BizAlarmRecord buildAlarm(BizDevice device, BizDeviceMonitorData data,
                                      BizAlarmRule rule, BigDecimal actual) {
        BizAlarmRecord alarm = new BizAlarmRecord();
        alarm.setAlarmNo("AL" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        alarm.setProjectId(device.getProjectId());
        alarm.setDeviceId(device.getId());
        alarm.setRuleId(rule.getId());
        alarm.setAlarmType("DEVICE_THRESHOLD");
        alarm.setAlarmLevel(rule.getAlarmLevel());
        alarm.setAlarmTitle(device.getDeviceName() + "触发" + rule.getRuleName());
        alarm.setAlarmContent(rule.getMetricName() + " 实际值 " + actual + "，阈值 " + rule.getThresholdValue());
        alarm.setThresholdValue(rule.getThresholdValue());
        alarm.setActualValue(actual);
        alarm.setAlarmStatus("PENDING");
        alarm.setTriggeredAt(data.getCollectedAt());
        return alarm;
    }

    /**
     * 转换为接口返回对象。
     */
    private AlarmRecordVO toVO(BizAlarmRecord alarm) {
        AlarmRecordVO vo = new AlarmRecordVO();
        BeanUtils.copyProperties(alarm, vo);
        return vo;
    }
}
