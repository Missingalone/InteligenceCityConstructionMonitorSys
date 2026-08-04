package com.cyx.system.entity.vo;

import lombok.Data;

@Data
public class RoleVO {
    private Long id;
    private String roleName;
    private String roleCode;
    private String dataScope;
    private Integer status;
    private String remark;
}
