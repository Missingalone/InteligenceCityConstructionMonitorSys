package com.cyx.system.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * 菜单返回对象 — 树形查询时通过 children 嵌套子菜单。
 */
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
    /** 子菜单列表 — 仅在 tree() 接口返回时填充 */
    private List<MenuVO> children;
}
