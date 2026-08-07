package com.cyx.supervisorterminal.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** 监管人员对企业整改结果的复查请求。 */
@Data
public class RectificationReviewDTO {
    private boolean approved;
    @NotBlank private String reviewRemark;
}
