package com.cyx.system.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MenuSaveDTO {
    private Long id;
    private Long parentId = 0L;
    @NotBlank
    private String menuName;
    @NotBlank
    private String menuType;
    private String routePath;
    private String component;
    private String permissionCode;
    private String icon;
    private Integer sortOrder = 0;
    private Integer visible = 1;
    private Integer status = 1;
}
