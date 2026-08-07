package com.cyx.enterpriseterminal.entity.vo;
import lombok.Data; import java.time.LocalDateTime;
/** 本企业整改通知单概览。 */
@Data public class EnterpriseRectificationVO { private Long id; private String orderNo; private Long projectId; private String title; private String content; private LocalDateTime deadlineAt; private String status; private String resultDescription; private String evidenceUrls; private String reviewRemark; }
