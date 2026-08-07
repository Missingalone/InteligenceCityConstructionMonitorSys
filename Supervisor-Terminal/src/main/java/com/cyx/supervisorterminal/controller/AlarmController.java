package com.cyx.supervisorterminal.controller;

import com.cyx.result.Result;
import com.cyx.supervisorterminal.entity.dto.AlarmHandleDTO;
import com.cyx.supervisorterminal.entity.vo.AlarmRecordVO;
import com.cyx.supervisorterminal.service.AlarmService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 监管端告警查询与处理接口。
 */
@RestController
@RequestMapping("/supervisor/alarms")
public class AlarmController {
    private final AlarmService alarmService;

    public AlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    /**
     * 查询当前账号数据范围内的告警。
     */
    @GetMapping
    @PreAuthorize("hasAuthority('supervisor:alarm:list')")
    public Result<List<AlarmRecordVO>> list() {
        return Result.success(alarmService.list());
    }

    /**
     * 记录告警处理进度或处理完成结果。
     */
    @PutMapping("/{id}/handle")
    @PreAuthorize("hasAuthority('supervisor:alarm:handle')")
    public Result<Void> handle(@PathVariable Long id, @Valid @RequestBody AlarmHandleDTO dto) {
        alarmService.handle(id, dto);
        return Result.success("处理成功", null);
    }
}
