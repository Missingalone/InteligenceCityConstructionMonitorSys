package com.cyx.supervisorterminal.controller;

import com.cyx.result.Result;
import com.cyx.supervisorterminal.entity.dto.AlarmRuleSaveDTO;
import com.cyx.supervisorterminal.entity.vo.AlarmRuleVO;
import com.cyx.supervisorterminal.service.AlarmRuleService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 监管端告警规则配置接口。
 */
@RestController
@RequestMapping("/supervisor/alarm-rules")
public class AlarmRuleController {
    private final AlarmRuleService ruleService;

    public AlarmRuleController(AlarmRuleService ruleService) {
        this.ruleService = ruleService;
    }

    /**
     * 查询有效告警规则。
     */
    @GetMapping
    @PreAuthorize("hasAuthority('supervisor:alarm-rule:list')")
    public Result<List<AlarmRuleVO>> list() {
        return Result.success(ruleService.list());
    }

    /**
     * 创建告警规则。
     */
    @PostMapping
    @PreAuthorize("hasAuthority('supervisor:alarm-rule:add')")
    public Result<Long> create(@Valid @RequestBody AlarmRuleSaveDTO dto) {
        return Result.success("创建成功", ruleService.create(dto));
    }

    /**
     * 修改告警规则。
     */
    @PutMapping
    @PreAuthorize("hasAuthority('supervisor:alarm-rule:edit')")
    public Result<Void> update(@Valid @RequestBody AlarmRuleSaveDTO dto) {
        ruleService.update(dto);
        return Result.success("修改成功", null);
    }

    /**
     * 删除告警规则。
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('supervisor:alarm-rule:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        ruleService.delete(id);
        return Result.success("删除成功", null);
    }
}
