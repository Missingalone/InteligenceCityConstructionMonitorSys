package com.cyx.supervisorterminal.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ConstructionFoundationVO {

    private String pidCode;
    private String pitName;
    private String pitType;
    private String excavationMethod;
    private BigDecimal area;
    private String supportType;
    private String supportScheme;
    private LocalDateTime excavationStartDate;
    private LocalDateTime excavationEndDate;
    private LocalDateTime backfillDate;
    private String currentStage;
    private BigDecimal progress;
    private String riskLevel;
    private Integer monitoringStatus;
    private String warningThreshold;
    private String alarmReason;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String location_address;
    private String responsiblePerson;
    private String responsiblePhone;
    private Text description;
    private String remark;
    private String createBy;
    private String updateBy;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
