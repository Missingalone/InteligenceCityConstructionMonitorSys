package com.cyx.system.entity.vo;

import lombok.Data;

@Data
public class MenuVO {
    private Long id;
    private Long parentId;
    private String menuName;
    private String menuType;
    private String routePath;
    private String component;
    private String permissionCode;
    private String icon;
    private Integer sortOrder;
    private Integer visible;
    private Integer status;
}
