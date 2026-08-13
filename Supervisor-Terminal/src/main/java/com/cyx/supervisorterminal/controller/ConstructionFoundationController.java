package com.cyx.supervisorterminal.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyx.result.Result;
import com.cyx.supervisorterminal.entity.dto.ConstructionUpdateDTO;
import com.cyx.supervisorterminal.entity.po.ConstructionFoundation;
import com.cyx.supervisorterminal.service.ConstructionFoundationService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/** 监管端施工基坑档案接口。 */
@RestController
@RequestMapping("/supervisor/foundation-pits")
public class ConstructionFoundationController {
    private final ConstructionFoundationService service;

    public ConstructionFoundationController(ConstructionFoundationService service) {
        this.service = service;
    }

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('supervisor:project:list')")
    public Result<Page<ConstructionFoundation>> page(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(service.getConstructionFoundationPage(pageNum, pageSize));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('supervisor:project:query')")
    public Result<ConstructionFoundation> details(@PathVariable Long id) {
        return Result.success(service.getConstructionFoundationDetails(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('supervisor:project:add')")
    public Result<Long> create(@Valid @RequestBody ConstructionUpdateDTO dto) {
        return Result.success("创建成功", service.createConstructionFoundation(dto));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('supervisor:project:edit')")
    public Result<Void> update(@Valid @RequestBody ConstructionUpdateDTO dto) {
        service.updateConstructionFoundation(dto);
        return Result.success("修改成功", null);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('supervisor:project:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        service.deleteConstructionFoundation(id);
        return Result.success("删除成功", null);
    }
}
