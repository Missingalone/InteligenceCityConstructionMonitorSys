package com.cyx.supervisorterminal.entity.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 告警记录返回对象。 */
@Data
public class AlarmRecordVO {
    private Long id; private String alarmNo; private Long projectId; private Long deviceId; private Long ruleId;
    private String alarmType; private String alarmLevel; private String alarmTitle; private String alarmContent;
    private BigDecimal thresholdValue; private BigDecimal actualValue; private String alarmStatus;
    private LocalDateTime triggeredAt; private LocalDateTime handledAt; private String handleRemark;
}
