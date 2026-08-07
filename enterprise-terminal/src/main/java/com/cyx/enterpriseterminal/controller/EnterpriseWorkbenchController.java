package com.cyx.enterpriseterminal.controller;
import com.cyx.result.Result; import com.cyx.enterpriseterminal.entity.dto.*; import com.cyx.enterpriseterminal.entity.vo.*; import com.cyx.enterpriseterminal.service.EnterpriseWorkbenchService; import jakarta.validation.Valid; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.List;
/** 施工企业项目、告警和整改工作台接口。 */
@RestController @RequestMapping("/enterprise")
public class EnterpriseWorkbenchController {
    private final EnterpriseWorkbenchService service; public EnterpriseWorkbenchController(EnterpriseWorkbenchService service){this.service=service;}
    /** 查询本企业项目。 */ @GetMapping("/projects") @PreAuthorize("hasAuthority('enterprise:project:list')") public Result<List<EnterpriseProjectVO>> projects(){return Result.success(service.projects());}
    /** 上报项目进度。 */ @PutMapping("/projects/{id}/progress") @PreAuthorize("hasAuthority('enterprise:project:progress')") public Result<Void> progress(@PathVariable Long id,@Valid @RequestBody ProjectProgressDTO dto){service.updateProgress(id,dto);return Result.success("上报成功",null);}
    /** 查询本企业项目告警。 */ @GetMapping("/alarms") @PreAuthorize("hasAuthority('enterprise:alarm:list')") public Result<List<EnterpriseAlarmVO>> alarms(){return Result.success(service.alarms());}
    /** 查询本企业整改通知单。 */ @GetMapping("/rectifications") @PreAuthorize("hasAuthority('enterprise:rectification:list')") public Result<List<EnterpriseRectificationVO>> rectifications(){return Result.success(service.rectifications());}
    /** 提交整改结果和佐证。 */ @PutMapping("/rectifications/{id}/submit") @PreAuthorize("hasAuthority('enterprise:rectification:submit')") public Result<Void> submit(@PathVariable Long id,@Valid @RequestBody RectificationSubmitDTO dto){service.submitRectification(id,dto);return Result.success("提交成功",null);}
}
