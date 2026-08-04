package com.cyx.system.controller;

import com.cyx.result.Result;
import com.cyx.system.entity.dto.RoleMenuAssignDTO;
import com.cyx.system.entity.dto.RoleSaveDTO;
import com.cyx.system.entity.vo.RoleVO;
import com.cyx.system.service.SysRoleService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/roles")
public class SysRoleController {

    private final SysRoleService roleService;

    public SysRoleController(SysRoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('system:role:list')")
    public Result<List<RoleVO>> list() {
        return Result.success(roleService.list());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('system:role:edit')")
    public Result<Long> save(@Valid @RequestBody RoleSaveDTO dto) {
        return Result.success("保存成功", roleService.save(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:role:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return Result.success("删除成功", null);
    }

    @PostMapping("/menus")
    @PreAuthorize("hasAuthority('system:role:authorize')")
    public Result<Void> assignMenus(@Valid @RequestBody RoleMenuAssignDTO dto) {
        roleService.assignMenus(dto);
        return Result.success("授权成功", null);
    }
}
