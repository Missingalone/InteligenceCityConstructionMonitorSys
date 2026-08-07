package com.cyx.supervisorterminal.entity.vo;

import lombok.Data;
import java.time.LocalDateTime;

/** 整改通知单返回对象。 */
@Data
public class RectificationOrderVO {
    private Long id; private String orderNo; private Long projectId; private Long alarmId; private Long enterpriseId;
    private String title; private String content; private LocalDateTime deadlineAt; private String status;
    private Long issuedBy; private LocalDateTime issuedAt; private LocalDateTime submittedAt;
    private String resultDescription; private String evidenceUrls; private LocalDateTime reviewedAt; private String reviewRemark;
}
