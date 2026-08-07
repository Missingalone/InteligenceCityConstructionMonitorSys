package com.cyx.publicterminal.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 公众反馈处理进度返回对象。
 */
@Data
public class PublicFeedbackVO {
    private String feedbackNo;
    private String feedbackType;
    private String content;
    private String status;
    private LocalDateTime handledAt;
    private String handleResult;
}
