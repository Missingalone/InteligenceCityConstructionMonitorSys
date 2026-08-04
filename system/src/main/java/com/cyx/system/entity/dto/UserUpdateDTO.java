package com.cyx.system.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UserUpdateDTO {
    @NotNull
    private Long id;
    @NotBlank
    private String realName;
    @NotNull
    private Long organizationId;
    @NotBlank
    private String userType;
    private String mobile;
    private String email;
    private Integer status;
    private List<Long> roleIds;
}
