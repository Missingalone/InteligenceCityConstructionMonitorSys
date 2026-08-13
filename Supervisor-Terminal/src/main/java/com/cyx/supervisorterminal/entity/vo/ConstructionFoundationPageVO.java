package com.cyx.supervisorterminal.entity.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ConstructionFoundationPageVO {
    private String pidCode;
    private String pitName;
    private String pitType;
    private LocalDateTime excavationStartDate;
    private LocalDateTime excavationEndDate;
    private LocalDateTime backfillDate;
    private String currentStage;
    private BigDecimal progress;
    private String riskLevel;
    private Integer monitoringStatus;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
