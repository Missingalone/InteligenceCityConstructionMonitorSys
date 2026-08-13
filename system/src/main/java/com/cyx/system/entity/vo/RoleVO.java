package com.cyx.system.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * 角色返回对象 — 详情查询时附带已分配的菜单ID列表。
 */
@Data
public class RoleVO {
    private Long id;
    private String roleName;
    private String roleCode;
    private String dataScope;
    private Integer status;
    private String remark;
    /** 已分配的菜单ID列表，仅在 getById 时填充 */
    private List<Long> menuIds;
}
