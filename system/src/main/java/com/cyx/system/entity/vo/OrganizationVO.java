package com.cyx.system.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * 组织返回对象 — 树形查询时通过 children 嵌套下级组织。
 */
@Data
public class OrganizationVO {
    private Long id;
    private Long parentId;
    private String orgName;
    private String orgCode;
    private String orgType;
    private Integer sortOrder;
    private Integer status;
    /** 下级组织列表 — 仅在 tree() 接口返回时填充 */
    private List<OrganizationVO> children;
}
