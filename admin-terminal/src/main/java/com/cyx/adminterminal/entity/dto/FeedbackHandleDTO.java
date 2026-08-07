package com.cyx.adminterminal.entity.dto;
import jakarta.validation.constraints.NotBlank; import lombok.Data;
/** 管理员处理公众反馈请求。 */ @Data public class FeedbackHandleDTO { @NotBlank private String handleResult; }
