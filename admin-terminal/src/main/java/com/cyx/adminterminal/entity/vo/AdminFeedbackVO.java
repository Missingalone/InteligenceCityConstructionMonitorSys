package com.cyx.adminterminal.entity.vo;
import lombok.Data; import java.time.LocalDateTime;
/** 管理员查看的公众反馈记录。 */ @Data public class AdminFeedbackVO { private Long id; private String feedbackNo; private Long projectId; private String feedbackType; private String content; private String contactName; private String contactMobile; private String status; private LocalDateTime handledAt; private String handleResult; }
