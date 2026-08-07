package com.cyx.supervisorterminal.controller;

import com.cyx.result.Result;
import com.cyx.supervisorterminal.entity.dto.MonitorDataReportDTO;
import com.cyx.supervisorterminal.entity.vo.MonitorDataVO;
import com.cyx.supervisorterminal.service.MonitorDataService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 监管端设备监测数据接口。 */
@RestController
@RequestMapping("/supervisor/monitor-data")
public class MonitorDataController {
    private final MonitorDataService monitorDataService;

    public MonitorDataController(MonitorDataService monitorDataService) {
        this.monitorDataService = monitorDataService;
    }

    /** 保存设备或网关提交的一条环境采样。 */
    @PostMapping("/report")
    @PreAuthorize("hasAuthority('supervisor:monitor:report')")
    public Result<Long> report(@Valid @RequestBody MonitorDataReportDTO dto) {
        return Result.success("上报成功", monitorDataService.report(dto));
    }

    /** 查询当前账号可见项目的监测记录。 */
    @GetMapping
    @PreAuthorize("hasAuthority('supervisor:monitor:list')")
    public Result<List<MonitorDataVO>> list() {
        return Result.success(monitorDataService.list());
    }
}
