package com.cyx.system.entity.vo;

import lombok.Data;

@Data
public class OrganizationVO {
    private Long id;
    private Long parentId;
    private String orgName;
    private String orgCode;
    private String orgType;
    private Integer sortOrder;
    private Integer status;
}
