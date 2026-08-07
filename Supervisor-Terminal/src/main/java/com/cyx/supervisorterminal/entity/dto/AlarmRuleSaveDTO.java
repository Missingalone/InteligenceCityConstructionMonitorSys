package com.cyx.supervisorterminal.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/** 告警阈值规则新增或修改请求。 */
@Data
public class AlarmRuleSaveDTO {
    private Long id;
    @NotBlank private String ruleName;
    @NotBlank private String ruleCode;
    private String deviceType;
    @NotBlank private String metricName;
    @NotBlank private String comparisonOperator;
    @NotNull private BigDecimal thresholdValue;
    @NotBlank private String alarmLevel;
    private Integer enabled = 1;
    private String remark;
}
