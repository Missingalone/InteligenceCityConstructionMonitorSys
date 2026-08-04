package com.cyx.system.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrganizationSaveDTO {
    private Long id;
    private Long parentId = 0L;
    @NotBlank
    private String orgName;
    @NotBlank
    private String orgCode;
    @NotBlank
    private String orgType;
    private Integer sortOrder = 0;
    private Integer status = 1;
}
