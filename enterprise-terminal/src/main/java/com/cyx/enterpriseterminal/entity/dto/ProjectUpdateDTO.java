package com.cyx.enterpriseterminal.entity.dto;

import lombok.Data;
import java.math.BigDecimal;


@Data
public class ProjectUpdateDTO {

    private Long id;
    private String projectCode;
    private String projectName;
    private String enterpriseName;
    private String supervisorName;
    private String projectType;
    private String projectStatus;
    private String address;
    private String projectManager;
    private BigDecimal progressPercent;
    private String projectManagerPhone;
    private String description;
}
