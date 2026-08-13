package com.cyx.enterpriseterminal.entity.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProjectDetailsVO {

    private Long id;
    private String projectCode;
    private String projectName;
    private String enterpriseName;
    private String supervisorName;
    private String projectType;
    private String projectStatus;
    private String address;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private BigDecimal progressPercent;
    private String projectManager;
    private String projectManagerPhone;
    private String description;
    private LocalDateTime createTime;


}
