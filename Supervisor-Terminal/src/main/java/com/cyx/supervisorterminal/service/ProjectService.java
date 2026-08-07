package com.cyx.supervisorterminal.service;

import com.cyx.supervisorterminal.entity.dto.ProjectMemberAssignDTO;
import com.cyx.supervisorterminal.entity.dto.ProjectSaveDTO;
import com.cyx.supervisorterminal.entity.vo.ProjectVO;

import java.util.List;

/** 监管端项目档案和项目成员服务。 */
public interface ProjectService {
    /** 查询当前账号数据范围内的项目。 */
    List<ProjectVO> list();
    /** 查询项目详情和成员。 */
    ProjectVO getById(Long id);
    /** 创建项目。 */
    Long create(ProjectSaveDTO dto);
    /** 修改有权访问的项目。 */
    void update(ProjectSaveDTO dto);
    /** 删除项目及其成员关系。 */
    void delete(Long id);
    /** 全量替换项目成员列表。 */
    void replaceMembers(Long projectId, List<ProjectMemberAssignDTO> members);
}
