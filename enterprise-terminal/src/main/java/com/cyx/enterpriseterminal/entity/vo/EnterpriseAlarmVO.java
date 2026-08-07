package com.cyx.enterpriseterminal.entity.vo;
import lombok.Data; import java.math.BigDecimal; import java.time.LocalDateTime;
/** 本企业项目告警概览。 */
@Data public class EnterpriseAlarmVO { private Long id; private String alarmNo; private Long projectId; private String alarmLevel; private String alarmTitle; private String alarmContent; private BigDecimal actualValue; private String alarmStatus; private LocalDateTime triggeredAt; }
