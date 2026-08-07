package com.cyx.supervisorterminal.entity.vo;

import lombok.Data;
import java.math.BigDecimal;

/** 告警规则返回对象。 */
@Data
public class AlarmRuleVO {
    private Long id; private String ruleName; private String ruleCode; private String deviceType;
    private String metricName; private String comparisonOperator; private BigDecimal thresholdValue;
    private String alarmLevel; private Integer enabled; private String remark;
}
