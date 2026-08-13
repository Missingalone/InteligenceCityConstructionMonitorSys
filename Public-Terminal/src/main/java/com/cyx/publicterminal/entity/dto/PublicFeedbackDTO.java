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
    @Size(max = 30)
    private String feedbackType;
    @NotBlank
    @Size(max = 2000)
    private String content;
    @NotBlank
    @Size(max = 50)
    private String contactName;
    @NotBlank
    // 手机号会被用于公众查询反馈，限制格式可以避免无效联系方式进入业务表。
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String contactMobile;
    // 附件由外部文件服务上传，本接口只接收 JSON 地址列表字符串。
    @Size(max = 4000)
    private String attachmentUrls;
}
