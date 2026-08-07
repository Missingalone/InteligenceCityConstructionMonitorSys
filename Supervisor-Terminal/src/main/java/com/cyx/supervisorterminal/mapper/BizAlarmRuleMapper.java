package com.cyx.supervisorterminal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyx.supervisorterminal.entity.po.BizAlarmRule;
import org.apache.ibatis.annotations.Mapper;

/** 告警规则数据访问。 */
@Mapper
public interface BizAlarmRuleMapper extends BaseMapper<BizAlarmRule> {
}
