package com.cyx.enterpriseterminal.controller;

import com.cyx.enterpriseterminal.entity.dto.ProjectUpdateDTO;
import com.cyx.enterpriseterminal.entity.vo.ProjectDetailsVO;
import com.cyx.enterpriseterminal.service.ProjectService;
import com.cyx.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/enterprise/project")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/details/{projectId}")
    public Result<ProjectDetailsVO> getProjectDetails(@PathVariable("projectId") Long projectId){
        return Result.success(projectService.getProjectDetails(projectId));
    }
    @PutMapping("/update")
    public Result<Boolean> updateProject(@RequestBody ProjectUpdateDTO dto){
        return Result.success(projectService.updateProject(dto));
    }

    @DeleteMapping("/delete/{projectId}")
    public Result<Boolean> deleteProject(@PathVariable("projectId") Long projectId){
        return Result.success(projectService.deleteProject(projectId));
    }
}
