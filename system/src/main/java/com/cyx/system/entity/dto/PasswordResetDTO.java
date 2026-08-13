package com.cyx.system.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 密码重置请求 — 管理员操作，只需新密码和用户ID，不需要旧密码。
 */
@Data
public class PasswordResetDTO {
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    @NotBlank(message = "新密码不能为空")
    // 字符串长度必须使用 Size 校验；Min 只能校验数值大小。
    @Size(min = 8, max = 64, message = "密码长度必须为8到64位")
    private String newPassword;
}
