package com.cyx.system.controller;

import com.cyx.result.Result;
import com.cyx.system.entity.dto.MenuSaveDTO;
import com.cyx.system.entity.vo.MenuVO;
import com.cyx.system.service.SysMenuService;
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
@RequestMapping("/system/menus")
public class SysMenuController {

    private final SysMenuService menuService;

    public SysMenuController(SysMenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('system:menu:list')")
    public Result<List<MenuVO>> list() {
        return Result.success(menuService.list());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('system:menu:edit')")
    public Result<Long> save(@Valid @RequestBody MenuSaveDTO dto) {
        return Result.success("保存成功", menuService.save(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:menu:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        menuService.delete(id);
        return Result.success("删除成功", null);
    }
}
