package com.cyx.publicterminal.controller;

import com.cyx.result.Result;
import com.cyx.publicterminal.entity.dto.PublicFeedbackDTO;
import com.cyx.publicterminal.entity.vo.*;
import com.cyx.publicterminal.service.PublicPortalService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 无需登录的公众项目与反馈接口。
 */
@RestController
@RequestMapping("/public")
public class PublicPortalController {
    private final PublicPortalService service;

    public PublicPortalController(PublicPortalService s) {
        service = s;
    }

    /**
     * 查询公开项目信息。
     */
    @GetMapping("/projects")
    public Result<List<PublicProjectVO>> projects() {
        return Result.success(service.projects());
    }

    /**
     * 提交公众反馈。
     */
    @PostMapping("/feedback")
    public Result<String> submit(@Valid @RequestBody PublicFeedbackDTO dto) {
        return Result.success("提交成功", service.submit(dto));
    }

    /**
     * 使用反馈编号和手机号查询处理进度。
     */
    @GetMapping("/feedback/{no}")
    public Result<PublicFeedbackVO> feedback(@PathVariable String no, @RequestParam String mobile) {
        return Result.success(service.feedback(no, mobile));
    }
}
