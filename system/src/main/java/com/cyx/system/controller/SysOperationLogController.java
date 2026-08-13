package com.cyx.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cyx.result.Result;
import com.cyx.system.entity.vo.OperationLogVO;
import com.cyx.system.service.SysOperationLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 操作日志查询接口 — 仅管理员和审计角色可查看。
 * <p>
 * 日志由 AOP 切面自动产生，本控制器只提供只读查询。
 */
@RestController
@RequestMapping("/system/operation-logs")
public class SysOperationLogController {

    private final SysOperationLogService service;

    public SysOperationLogController(SysOperationLogService service) {
        this.service = service;
    }

    /**
     * 分页查询操作日志。
     *
     * @param page     页码，默认1
     * @param size     每页条数，默认20
     * @param username 操作人筛选（可选）
     * @param module   模块筛选（可选）
     */
    @GetMapping
    @PreAuthorize("hasAuthority('system:log:list')")
    public Result<IPage<OperationLogVO>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String module) {
        return Result.success(service.page(page, size, username, module));
    }
}
