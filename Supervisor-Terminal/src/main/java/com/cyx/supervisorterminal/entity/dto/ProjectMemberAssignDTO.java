package com.cyx.supervisorterminal.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectMemberAssignDTO {
    @NotNull
    private Long userId;
    @NotBlank
    private String memberRole;
}
