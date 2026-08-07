package com.cyx.enterpriseterminal.entity.vo;
import lombok.Data; import java.math.BigDecimal; import java.time.LocalDate;
/** 企业项目概览。 */
@Data public class EnterpriseProjectVO { private Long id; private String projectCode; private String projectName; private String projectStatus; private String address; private BigDecimal progressPercent; private LocalDate plannedStartDate; private LocalDate plannedEndDate; private LocalDate actualStartDate; }
