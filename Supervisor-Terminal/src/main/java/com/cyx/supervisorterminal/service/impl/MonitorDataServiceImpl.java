package com.cyx.supervisorterminal.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cyx.exception.BusException;
import com.cyx.supervisorterminal.entity.dto.MonitorDataReportDTO;
import com.cyx.supervisorterminal.entity.po.BizDevice;
import com.cyx.supervisorterminal.entity.po.BizDeviceMonitorData;
import com.cyx.supervisorterminal.entity.vo.MonitorDataVO;
import com.cyx.supervisorterminal.mapper.BizDeviceMapper;
import com.cyx.supervisorterminal.mapper.BizDeviceMonitorDataMapper;
import com.cyx.supervisorterminal.security.CurrentUserAccess;
import com.cyx.supervisorterminal.service.AlarmService;
import com.cyx.supervisorterminal.service.MonitorDataService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/** 负责将设备采样转换为平台时序数据，并维护设备在线状态。 */
@Service
public class MonitorDataServiceImpl implements MonitorDataService {
    private final BizDeviceMapper deviceMapper;
    private final BizDeviceMonitorDataMapper monitorDataMapper;
    private final CurrentUserAccess currentUserAccess;
    private final AlarmService alarmService;

    public MonitorDataServiceImpl(BizDeviceMapper deviceMapper, BizDeviceMonitorDataMapper monitorDataMapper,
                                  CurrentUserAccess currentUserAccess, AlarmService alarmService) {
        this.deviceMapper = deviceMapper;
        this.monitorDataMapper = monitorDataMapper;
        this.currentUserAccess = currentUserAccess;
        this.alarmService = alarmService;
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long report(MonitorDataReportDTO dto) {
        BizDevice device = deviceMapper.selectOne(Wrappers.<BizDevice>lambdaQuery()
                .eq(BizDevice::getDeviceCode, dto.getDeviceCode()));
        if (device == null) {
            throw new BusException("设备编码不存在");
        }
        BizDeviceMonitorData data = new BizDeviceMonitorData();
        BeanUtils.copyProperties(dto, data, "deviceCode", "collectedAt");
        data.setDeviceId(device.getId());
        data.setProjectId(device.getProjectId());
        data.setCollectedAt(dto.getCollectedAt() == null ? LocalDateTime.now() : dto.getCollectedAt());
        monitorDataMapper.insert(data);

        // 数据落库后立即评估规则，整段事务失败时监测记录和告警一起回滚。
        alarmService.evaluate(device, data);

        // 设备成功上报意味着通信正常，统一在此刷新最近在线时间。
        device.setStatus("ONLINE");
        device.setLastOnlineAt(data.getCollectedAt());
        deviceMapper.updateById(device);
        return data.getId();
    }

    /** {@inheritDoc} */
    @Override
    public List<MonitorDataVO> list() {
        List<BizDeviceMonitorData> data = currentUserAccess.canAccessAllProjects()
                ? monitorDataMapper.selectList(Wrappers.<BizDeviceMonitorData>lambdaQuery()
                .orderByDesc(BizDeviceMonitorData::getCollectedAt).orderByDesc(BizDeviceMonitorData::getId))
                : monitorDataMapper.selectByProjectMemberUsername(currentUserAccess.username());
        return data.stream().map(this::toVO).toList();
    }

    /** 将数据库对象转换为接口返回对象，避免输出原始报文。 */
    private MonitorDataVO toVO(BizDeviceMonitorData data) {
        MonitorDataVO vo = new MonitorDataVO();
        BeanUtils.copyProperties(data, vo);
        return vo;
    }
}
