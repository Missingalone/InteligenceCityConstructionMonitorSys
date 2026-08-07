package com.cyx.supervisorterminal.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** 监管人员处理告警时提交的结论。 */
@Data
public class AlarmHandleDTO {
    @NotBlank private String handleRemark;
    private boolean resolved;
}
