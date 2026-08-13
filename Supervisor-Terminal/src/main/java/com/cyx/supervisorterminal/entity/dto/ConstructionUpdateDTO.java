package com.cyx.supervisorterminal.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ConstructionUpdateDTO {
    private Long id;
    @NotBlank
    private String pitCode;
    @NotBlank
    private String pitName;
    @NotNull
    private Long projectId;
    private String pitType;
    private String excavationMethod;
    private BigDecimal area;
    private String supportType;
    private String supportScheme;
    private LocalDate excavationStartDate;
    private LocalDate excavationEndDate;
    private LocalDate backfillDate;
    private String currentStage;
    private BigDecimal progress;
    private String riskLevel;
    private Integer monitoringStatus;
    private String warningThreshold;
    private String alarmReason;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String locationAddress;
    private String responsiblePerson;
    private String responsiblePhone;
    private String description;
    private String remark;
}
