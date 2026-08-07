package com.cyx.publicterminal.entity.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 公众投诉、建议或咨询提交请求。
 */
@Data
public class PublicFeedbackDTO {
    private Long projectId;
    @NotBlank
    private String feedbackType;
    @NotBlank
    @Size(max = 2000)
    private String content;
    @NotBlank
    private String contactName;
    @NotBlank
    private String contactMobile;
    private String attachmentUrls;
}
