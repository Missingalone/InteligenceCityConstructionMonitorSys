package com.cyx.supervisorterminal.entity.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

/** 监管人员下发整改通知单请求。 */
@Data
public class RectificationIssueDTO {
    @NotNull private Long alarmId;
    @NotBlank private String title;
    @NotBlank private String content;
    @NotNull @Future private LocalDateTime deadlineAt;
}
