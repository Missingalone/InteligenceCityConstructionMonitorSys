package com.cyx.supervisorterminal.controller;

import com.cyx.result.Result;
import com.cyx.supervisorterminal.entity.dto.EnterpriseSaveDTO;
import com.cyx.supervisorterminal.entity.vo.EnterpriseVO;
import com.cyx.supervisorterminal.service.EnterpriseService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 监管端施工企业档案接口。 */
@RestController
@RequestMapping("/supervisor/enterprises")
public class EnterpriseController {
    private final EnterpriseService enterpriseService;

    public EnterpriseController(EnterpriseService enterpriseService) {
        this.enterpriseService = enterpriseService;
    }

    /** 查询当前账号可见施工企业。 */
    @GetMapping
    @PreAuthorize("hasAuthority('supervisor:enterprise:list')")
    public Result<List<EnterpriseVO>> list() {
        return Result.success(enterpriseService.list());
    }

    /** 查询施工企业详情。 */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('supervisor:enterprise:query')")
    public Result<EnterpriseVO> getById(@PathVariable Long id) {
        return Result.success(enterpriseService.getById(id));
    }

    /** 创建施工企业档案。 */
    @PostMapping
    @PreAuthorize("hasAuthority('supervisor:enterprise:add')")
    public Result<Long> create(@Valid @RequestBody EnterpriseSaveDTO dto) {
        return Result.success("创建成功", enterpriseService.create(dto));
    }

    /** 修改施工企业档案。 */
    @PutMapping
    @PreAuthorize("hasAuthority('supervisor:enterprise:edit')")
    public Result<Void> update(@Valid @RequestBody EnterpriseSaveDTO dto) {
        enterpriseService.update(dto);
        return Result.success("修改成功", null);
    }

    /** 删除未关联项目的施工企业。 */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('supervisor:enterprise:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        enterpriseService.delete(id);
        return Result.success("删除成功", null);
    }
}
