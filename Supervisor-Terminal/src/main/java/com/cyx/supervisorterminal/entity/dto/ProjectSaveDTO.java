package com.cyx.supervisorterminal.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProjectSaveDTO {
    private Long id;
    @NotBlank
    private String projectCode;
    @NotBlank
    private String projectName;
    @NotNull
    private Long enterpriseId;
    @NotNull
    private Long supervisorOrgId;
    @NotBlank
    private String projectType;
    private String projectStatus = "PREPARING";
    private String address;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
    private LocalDate actualStartDate;
    private BigDecimal progressPercent = BigDecimal.ZERO;
    private String projectManager;
    private String managerMobile;
    private String description;
}
