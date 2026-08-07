package com.cyx.supervisorterminal.controller;

import com.cyx.result.Result;
import com.cyx.supervisorterminal.entity.dto.*;
import com.cyx.supervisorterminal.entity.vo.RectificationOrderVO;
import com.cyx.supervisorterminal.service.RectificationService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/** 监管端整改通知下发与复查接口。 */
@RestController @RequestMapping("/supervisor/rectifications")
public class RectificationController {
    private final RectificationService service;
    public RectificationController(RectificationService service) { this.service = service; }

    /** 查询当前账号可见整改单。 */
    @GetMapping @PreAuthorize("hasAuthority('supervisor:rectification:list')")
    public Result<List<RectificationOrderVO>> list() { return Result.success(service.list()); }

    /** 从指定告警下发整改通知。 */
    @PostMapping @PreAuthorize("hasAuthority('supervisor:rectification:issue')")
    public Result<Long> issue(@Valid @RequestBody RectificationIssueDTO dto) {
        return Result.success("下发成功", service.issue(dto));
    }

    /** 复查施工企业提交的整改结果。 */
    @PutMapping("/{id}/review") @PreAuthorize("hasAuthority('supervisor:rectification:review')")
    public Result<Void> review(@PathVariable Long id, @Valid @RequestBody RectificationReviewDTO dto) {
        service.review(id, dto); return Result.success("复查完成", null);
    }
}
