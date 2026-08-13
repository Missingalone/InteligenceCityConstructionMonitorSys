package com.cyx.system.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UserSaveDTO {
    @NotBlank
    private String username;
    @NotBlank
    @Size(min = 8, max = 64, message = "密码长度必须为8到64位")
    private String password;
    @NotBlank
    private String realName;
    @NotNull
    private Long organizationId;
    @NotBlank
    private String userType;
    private String mobile;
    private String email;
    private Integer status = 1;
    private List<Long> roleIds;
}
