package com.cyx.enterpriseterminal.service;

import com.cyx.enterpriseterminal.entity.dto.ProjectUpdateDTO;
import com.cyx.enterpriseterminal.entity.vo.ProjectDetailsVO;

public interface ProjectService {

    /**
     * 查询项目详情
     * @param projectId
     * @return
     */
    ProjectDetailsVO getProjectDetails(Long projectId);

    /**
     * 更新项目
     * @param dto
     * @return
     */
    Boolean updateProject(ProjectUpdateDTO dto);

    /**
     * 删除项目
     * @param projectId
     * @return
     */
    Boolean deleteProject(Long projectId);
}
