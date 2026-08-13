package com.cyx.adminterminal.controller;

import com.cyx.result.Result;
import com.cyx.adminterminal.entity.dto.FeedbackHandleDTO;
import com.cyx.adminterminal.entity.vo.*;
import com.cyx.adminterminal.service.AdminOperationService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员运营总览和公众反馈接口。
 */
@RestController
@RequestMapping("/admin")
public class AdminOperationController {
    private final AdminOperationService service;

    public AdminOperationController(AdminOperationService service) {
        this.service = service;
    }

    /**
     * 查询运营总览。
     */
    @GetMapping("/dashboard")
//    @PreAuthorize("hasAuthority('admin:dashboard:view')")
    public Result<DashboardStatsVO> dashboard() {
        return Result.success(service.dashboard());
    }

    /**
     * 查询公众反馈。
     */
    @GetMapping("/feedback")
    @PreAuthorize("hasAuthority('admin:feedback:list')")
    public Result<List<AdminFeedbackVO>> feedback() {
        return Result.success(service.feedback());
    }

    /**
     * 处理公众反馈。
     */
    @PutMapping("/feedback/{id}/handle")
    @PreAuthorize("hasAuthority('admin:feedback:handle')")
    public Result<Void> handle(@PathVariable Long id, @Valid @RequestBody FeedbackHandleDTO dto) {
        service.handleFeedback(id, dto);
        return Result.success("处理成功", null);
    }
}
