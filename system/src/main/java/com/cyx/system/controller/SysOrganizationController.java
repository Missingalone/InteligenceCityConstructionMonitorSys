package com.cyx.system.controller;

import com.cyx.result.Result;
import com.cyx.system.entity.dto.OrganizationSaveDTO;
import com.cyx.system.entity.vo.OrganizationVO;
import com.cyx.system.service.SysOrganizationService;
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
@RequestMapping("/system/organizations")
public class SysOrganizationController {

    private final SysOrganizationService organizationService;

    public SysOrganizationController(SysOrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('system:organization:list')")
    public Result<List<OrganizationVO>> list() {
        return Result.success(organizationService.list());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('system:organization:edit')")
    public Result<Long> save(@Valid @RequestBody OrganizationSaveDTO dto) {
        return Result.success("保存成功", organizationService.save(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:organization:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        organizationService.delete(id);
        return Result.success("删除成功", null);
    }
}
