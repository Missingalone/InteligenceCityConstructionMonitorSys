package com.cyx.system.controller;

import com.cyx.annotation.Log;
import com.cyx.result.Result;
import com.cyx.system.entity.dto.RoleMenuAssignDTO;
import com.cyx.system.entity.dto.RoleSaveDTO;
import com.cyx.system.entity.vo.RoleVO;
import com.cyx.system.service.SysRoleService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理接口 — 角色 CRUD 及菜单授权。
 */
@RestController
@RequestMapping("/system/roles")
public class SysRoleController {

    private final SysRoleService roleService;

    public SysRoleController(SysRoleService roleService) {
        this.roleService = roleService;
    }

    /** 查询全部角色 */
    @GetMapping
    @PreAuthorize("hasAuthority('system:role:list')")
    public Result<List<RoleVO>> list() {
        return Result.success(roleService.list());
    }

    /** 查询角色详情（含已分配的菜单ID列表） */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:role:query')")
    public Result<RoleVO> getById(@PathVariable Long id) {
        return Result.success(roleService.getById(id));
    }

    /** 新增或修改角色 */
    @Log(module = "角色管理", operation = "保存角色")
    @PostMapping
    @PreAuthorize("hasAuthority('system:role:edit')")
    public Result<Long> save(@Valid @RequestBody RoleSaveDTO dto) {
        return Result.success("保存成功", roleService.save(dto));
    }

    /** 删除角色 */
    @Log(module = "角色管理", operation = "删除角色")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:role:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return Result.success("删除成功", null);
    }

    /** 为角色分配菜单权限 */
    @Log(module = "角色管理", operation = "分配菜单权限")
    @PostMapping("/menus")
    @PreAuthorize("hasAuthority('system:role:authorize')")
    public Result<Void> assignMenus(@Valid @RequestBody RoleMenuAssignDTO dto) {
        roleService.assignMenus(dto);
        return Result.success("授权成功", null);
    }
}
