package com.cyx.supervisorterminal.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/** 设备监测指标的阈值告警规则。 */
@Data
@TableName("biz_alarm_rule")
public class BizAlarmRule {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String ruleName;
    private String ruleCode;
    private String deviceType;
    private String metricName;
    private String comparisonOperator;
    private BigDecimal thresholdValue;
    private String alarmLevel;
    private Integer enabled;
    private String remark;
    @TableLogic
    private Integer deleted;
}
