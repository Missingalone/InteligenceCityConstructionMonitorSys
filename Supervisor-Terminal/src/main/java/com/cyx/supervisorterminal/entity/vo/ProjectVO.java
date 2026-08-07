package com.cyx.supervisorterminal.entity.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class ProjectVO {
    private Long id;
    private String projectCode;
    private String projectName;
    private Long enterpriseId;
    private String enterpriseName;
    private Long supervisorOrgId;
    private String projectType;
    private String projectStatus;
    private String address;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
    private LocalDate actualStartDate;
    private BigDecimal progressPercent;
    private String projectManager;
    private String managerMobile;
    private String description;
    private List<ProjectMemberVO> members;
}
