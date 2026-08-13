package com.cyx.system.controller;

import com.cyx.annotation.Log;
import com.cyx.result.Result;
import com.cyx.system.entity.dto.OrganizationSaveDTO;
import com.cyx.system.entity.vo.OrganizationVO;
import com.cyx.system.service.SysOrganizationService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 组织管理接口 — 维护机构树形结构。
 * <p>
 * 前端使用 tree 接口获取组织树渲染组织选择器；
 * list 接口供管理页面做组织数据表格展示。
 */
@RestController
@RequestMapping("/system/organizations")
public class SysOrganizationController {

    private final SysOrganizationService organizationService;

    public SysOrganizationController(SysOrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    /** 查询组织平铺列表 */
    @GetMapping
    @PreAuthorize("hasAuthority('system:organization:list')")
    public Result<List<OrganizationVO>> list() {
        return Result.success(organizationService.list());
    }

    /** 查询组织树 */
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('system:organization:list')")
    public Result<List<OrganizationVO>> tree() {
        return Result.success(organizationService.tree());
    }

    /** 新增或修改组织 */
    @Log(module = "组织管理", operation = "保存组织")
    @PostMapping
    @PreAuthorize("hasAuthority('system:organization:edit')")
    public Result<Long> save(@Valid @RequestBody OrganizationSaveDTO dto) {
        return Result.success("保存成功", organizationService.save(dto));
    }

    /** 删除组织（有下级组织时拒绝删除） */
    @Log(module = "组织管理", operation = "删除组织")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:organization:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        organizationService.delete(id);
        return Result.success("删除成功", null);
    }
}
