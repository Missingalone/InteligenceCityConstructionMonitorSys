package com.cyx.enterpriseterminal.service.impl;

import com.cyx.enterpriseterminal.entity.dto.ProjectUpdateDTO;
import com.cyx.enterpriseterminal.entity.po.Project;
import com.cyx.enterpriseterminal.entity.vo.ProjectDetailsVO;
import com.cyx.enterpriseterminal.mapper.ProjectMapper;
import com.cyx.enterpriseterminal.service.ProjectService;
import com.cyx.exception.BusException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectMapper projectMapper;
    @Override
    public ProjectDetailsVO getProjectDetails(Long projectId) {
        ProjectDetailsVO detail = projectMapper.selectProjectDetails(projectId);
        if (detail == null) {
            throw new BusException("项目不存在或已删除");
        }
        return detail;
    }

    @Override
    public Boolean updateProject(ProjectUpdateDTO dto) {
        Project project = projectMapper.selectById(dto.getId());
        if (project == null) {
            throw new BusException("项目不存在或已删除");
        }
        project.setProjectCode(dto.getProjectCode());
        project.setProjectName(dto.getProjectName());
        project.setProjectType(dto.getProjectType());
        project.setProjectStatus(dto.getProjectStatus());
        project.setAddress(dto.getAddress());
        project.setProjectManager(dto.getProjectManager());
        project.setManagerMobile(dto.getProjectManagerPhone());
        project.setProgressPercent(dto.getProgressPercent());
        project.setDescription(dto.getDescription());
        return projectMapper.updateById(project) > 0;
    }

    @Override
    public Boolean deleteProject(Long projectId) {
        return projectMapper.deleteById(projectId)>0;
    }
}
