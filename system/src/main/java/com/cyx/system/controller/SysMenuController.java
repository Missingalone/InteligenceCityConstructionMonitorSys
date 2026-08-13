package com.cyx.system.controller;

import com.cyx.annotation.Log;
import com.cyx.result.Result;
import com.cyx.system.entity.dto.MenuSaveDTO;
import com.cyx.system.entity.vo.MenuVO;
import com.cyx.system.service.SysMenuService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理接口 — 维护前端路由菜单和按钮权限标识。
 * <p>
 * 前端使用 tree 接口获取完整菜单树渲染侧边栏导航；
 * list 接口供系统管理员做菜单数据维护（平铺表格）。
 */
@RestController
@RequestMapping("/system/menus")
public class  SysMenuController {

    private final SysMenuService menuService;

    public SysMenuController(SysMenuService menuService) {
        this.menuService = menuService;
    }

    /** 查询菜单平铺列表 */
    @GetMapping
    @PreAuthorize("hasAuthority('system:menu:list')")
    public Result<List<MenuVO>> list() {
        return Result.success(menuService.list());
    }

    /** 查询菜单树（前端渲染 el-tree 使用） */
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('system:menu:list')")
    public Result<List<MenuVO>> tree() {
        return Result.success(menuService.tree());
    }

    /** 新增或修改菜单 */
    @Log(module = "菜单管理", operation = "保存菜单")
    @PostMapping
    @PreAuthorize("hasAuthority('system:menu:edit')")
    public Result<Long> save(@Valid @RequestBody MenuSaveDTO dto) {
        return Result.success("保存成功", menuService.save(dto));
    }

    /** 删除菜单（有子菜单时拒绝删除） */
    @Log(module = "菜单管理", operation = "删除菜单")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:menu:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        menuService.delete(id);
        return Result.success("删除成功", null);
    }
}
