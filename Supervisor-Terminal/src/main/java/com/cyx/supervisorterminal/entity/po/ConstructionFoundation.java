package com.cyx.supervisorterminal.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("construction_foundation_pit")
public class ConstructionFoundation {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String pitCode;
    private String pitName;
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
    private Long createdBy;
    private LocalDateTime createdTime;
    private Long updatedBy;
    private LocalDateTime updatedTime;
    @TableLogic
    private Integer deleted;
}
