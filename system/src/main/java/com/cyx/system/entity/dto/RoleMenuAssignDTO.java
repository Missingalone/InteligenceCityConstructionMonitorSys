package com.cyx.system.entity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RoleMenuAssignDTO {
    @NotNull
    private Long roleId;
    private List<Long> menuIds;
}
