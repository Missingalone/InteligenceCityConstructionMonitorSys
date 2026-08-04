package com.cyx.system.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserVO {
    private Long id;
    private Long organizationId;
    private String username;
    private String realName;
    private String mobile;
    private String email;
    private String userType;
    private Integer status;
    private List<String> roleCodes;
}
