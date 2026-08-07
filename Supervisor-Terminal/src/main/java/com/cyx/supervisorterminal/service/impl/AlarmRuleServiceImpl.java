package com.cyx.supervisorterminal.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cyx.exception.BusException;
import com.cyx.supervisorterminal.entity.dto.AlarmRuleSaveDTO;
import com.cyx.supervisorterminal.entity.po.BizAlarmRule;
import com.cyx.supervisorterminal.entity.vo.AlarmRuleVO;
import com.cyx.supervisorterminal.mapper.BizAlarmRuleMapper;
import com.cyx.supervisorterminal.service.AlarmRuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/** 告警规则配置服务实现，负责规则唯一性和规则参数校验。 */
@Service
public class AlarmRuleServiceImpl implements AlarmRuleService {
    private static final Set<String> METRICS = Set.of("pm25", "pm10", "noiseDb", "temperature", "humidity", "windSpeed");
    private static final Set<String> OPERATORS = Set.of("GT", "GTE", "LT", "LTE", "EQ");
    private final BizAlarmRuleMapper ruleMapper;

    public AlarmRuleServiceImpl(BizAlarmRuleMapper ruleMapper) {
        this.ruleMapper = ruleMapper;
    }

    /** {@inheritDoc} */
    @Override
    public List<AlarmRuleVO> list() {
        return ruleMapper.selectList(Wrappers.<BizAlarmRule>lambdaQuery().orderByDesc(BizAlarmRule::getId))
                .stream().map(this::toVO).toList();
    }

    /** {@inheritDoc} */
    @Override
    public Long create(AlarmRuleSaveDTO dto) {
        validate(dto);
        ensureCodeAvailable(dto.getRuleCode(), null);
        BizAlarmRule rule = new BizAlarmRule();
        BeanUtils.copyProperties(dto, rule, "id");
        ruleMapper.insert(rule);
        return rule.getId();
    }

    /** {@inheritDoc} */
    @Override
    public void update(AlarmRuleSaveDTO dto) {
        if (dto.getId() == null || ruleMapper.selectById(dto.getId()) == null) {
            throw new BusException("告警规则不存在");
        }
        validate(dto);
        ensureCodeAvailable(dto.getRuleCode(), dto.getId());
        BizAlarmRule rule = new BizAlarmRule();
        BeanUtils.copyProperties(dto, rule);
        ruleMapper.updateById(rule);
    }

    /** {@inheritDoc} */
    @Override
    public void delete(Long id) {
        if (ruleMapper.selectById(id) == null) {
            throw new BusException("告警规则不存在");
        }
        ruleMapper.deleteById(id);
    }

    /** 校验指标名和运算符，防止配置无法执行的规则。 */
    private void validate(AlarmRuleSaveDTO dto) {
        if (!METRICS.contains(dto.getMetricName())) {
            throw new BusException("不支持的监测指标");
        }
        if (!OPERATORS.contains(dto.getComparisonOperator())) {
            throw new BusException("不支持的比较运算符");
        }
    }

    /** 校验规则编码唯一，更新时排除自身。 */
    private void ensureCodeAvailable(String code, Long excludeId) {
        long count = ruleMapper.selectCount(Wrappers.<BizAlarmRule>lambdaQuery()
                .eq(BizAlarmRule::getRuleCode, code).ne(excludeId != null, BizAlarmRule::getId, excludeId));
        if (count > 0) {
            throw new BusException("告警规则编码已存在");
        }
    }

    /** 转换为接口返回对象。 */
    private AlarmRuleVO toVO(BizAlarmRule rule) {
        AlarmRuleVO vo = new AlarmRuleVO();
        BeanUtils.copyProperties(rule, vo);
        return vo;
    }
}
