package com.cyx.system.controller;

import com.cyx.annotation.Log;
import com.cyx.result.Result;
import com.cyx.system.entity.dto.PasswordResetDTO;
import com.cyx.system.entity.dto.UserSaveDTO;
import com.cyx.system.entity.dto.UserUpdateDTO;
import com.cyx.system.entity.vo.UserVO;
import com.cyx.system.service.SysUserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户管理接口 — 系统用户的 CRUD。
 * <p>
 * 所有写操作通过 @Log 注解自动记录审计日志。
 */
@RestController
@RequestMapping("/system/users")
public class SysUserController {

    private final SysUserService userService;

    public SysUserController(SysUserService userService) {
        this.userService = userService;
    }

    /** 查询用户列表 */
    @GetMapping
    @PreAuthorize("hasAuthority('system:user:list')")
    public Result<List<UserVO>> list() {
        return Result.success(userService.list());
    }

    /** 查询用户详情 */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:user:query')")
    public Result<UserVO> getById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    /** 创建用户 */
    @Log(module = "用户管理", operation = "创建用户")
    @PostMapping
    @PreAuthorize("hasAuthority('system:user:add')")
    public Result<Long> create(@Valid @RequestBody UserSaveDTO dto) {
        return Result.success("创建成功", userService.create(dto));
    }

    /** 修改用户信息 */
    @Log(module = "用户管理", operation = "修改用户")
    @PutMapping
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Result<Void> update(@Valid @RequestBody UserUpdateDTO dto) {
        userService.update(dto);
        return Result.success("修改成功", null);
    }

    /** 删除用户 */
    @Log(module = "用户管理", operation = "删除用户")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:user:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.success("删除成功", null);
    }

    /**
     * 重置用户密码 — 管理员操作，不需要旧密码。
     * 前端提交 {@link PasswordResetDTO}，包含用户ID和新密码。
     */
    @Log(module = "用户管理", operation = "重置密码")
    @PutMapping("/reset-password")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Result<Void> resetPassword(@Valid @RequestBody PasswordResetDTO dto) {
        userService.resetPassword(dto.getUserId(), dto.getNewPassword());
        return Result.success("密码重置成功", null);
    }

    /**
     * 切换用户启用/停用状态。
     * 停用后该用户无法通过认证（即使 token 未过期）。
     */
    @Log(module = "用户管理", operation = "切换用户状态")
    @PutMapping("/{id}/toggle-status")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Result<Map<String, Object>> toggleStatus(@PathVariable Long id) {
        userService.toggleStatus(id);
        return Result.success("状态切换成功", null);
    }
}
