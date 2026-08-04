package com.cyx.system.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleSaveDTO {
    private Long id;
    @NotBlank
    private String roleName;
    @NotBlank
    private String roleCode;
    private String dataScope = "SELF";
    private Integer status = 1;
    private String remark;
}
