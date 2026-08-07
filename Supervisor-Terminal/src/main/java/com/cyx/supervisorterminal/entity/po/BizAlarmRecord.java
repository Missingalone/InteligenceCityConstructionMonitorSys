package com.cyx.supervisorterminal.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 一次规则命中的业务告警。 */
@Data
@TableName("biz_alarm_record")
public class BizAlarmRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String alarmNo;
    private Long projectId;
    private Long deviceId;
    private Long ruleId;
    private String alarmType;
    private String alarmLevel;
    private String alarmTitle;
    private String alarmContent;
    private BigDecimal thresholdValue;
    private BigDecimal actualValue;
    private String alarmStatus;
    private Long handlerId;
    private LocalDateTime handledAt;
    private String handleRemark;
    private LocalDateTime triggeredAt;
    @TableLogic
    private Integer deleted;
}
