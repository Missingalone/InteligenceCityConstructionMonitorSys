package com.cyx.supervisorterminal.controller;

import com.cyx.result.Result;
import com.cyx.supervisorterminal.entity.dto.ProjectMemberAssignDTO;
import com.cyx.supervisorterminal.entity.dto.ProjectSaveDTO;
import com.cyx.supervisorterminal.entity.vo.ProjectVO;
import com.cyx.supervisorterminal.service.ProjectService;
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

/** 监管端项目档案与成员分配接口。 */
@RestController
@RequestMapping("/supervisor/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /** 查询当前账号数据范围内项目。 */
    @GetMapping
    @PreAuthorize("hasAuthority('supervisor:project:list')")
    public Result<List<ProjectVO>> list() {
        return Result.success(projectService.list());
    }

    /** 查询项目详情和成员。 */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('supervisor:project:query')")
    public Result<ProjectVO> getById(@PathVariable Long id) {
        return Result.success(projectService.getById(id));
    }

    /** 创建建设项目。 */
    @PostMapping
    @PreAuthorize("hasAuthority('supervisor:project:add')")
    public Result<Long> create(@Valid @RequestBody ProjectSaveDTO dto) {
        return Result.success("创建成功", projectService.create(dto));
    }

    /** 修改建设项目。 */
    @PutMapping
    @PreAuthorize("hasAuthority('supervisor:project:edit')")
    public Result<Void> update(@Valid @RequestBody ProjectSaveDTO dto) {
        projectService.update(dto);
        return Result.success("修改成功", null);
    }

    /** 删除项目及成员关系。 */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('supervisor:project:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return Result.success("删除成功", null);
    }

    /** 全量替换项目成员。 */
    @PutMapping("/{id}/members")
    @PreAuthorize("hasAuthority('supervisor:project:assign-member')")
    public Result<Void> replaceMembers(@PathVariable Long id,
                                       @Valid @RequestBody List<ProjectMemberAssignDTO> members) {
        projectService.replaceMembers(id, members);
        return Result.success("成员分配成功", null);
    }
}
