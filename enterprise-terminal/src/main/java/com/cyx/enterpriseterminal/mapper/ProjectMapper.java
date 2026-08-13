package com.cyx.enterpriseterminal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyx.enterpriseterminal.entity.po.Project;
import com.cyx.enterpriseterminal.entity.vo.ProjectDetailsVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


public interface ProjectMapper extends BaseMapper<Project> {
    @Select("""
            SELECT p.id,
                   p.project_code,
                   p.project_name,
                   e.enterprise_name,
                   o.org_name AS supervisor_name,
                   p.project_type,
                   p.project_status,
                   p.address,
                   p.longitude,
                   p.latitude,
                   p.planned_start_date,
                   p.planned_end_date,
                   p.actual_start_date,
                   p.progress_percent,
                   p.project_manager,
                   p.manager_mobile AS project_manager_phone,
                   p.description,
                   p.created_at AS create_time
            FROM biz_project p
            LEFT JOIN biz_enterprise e ON e.id = p.enterprise_id AND e.deleted = 0
            LEFT JOIN sys_organization o ON o.id = p.supervisor_org_id AND o.deleted = 0
            WHERE p.id = #{projectId} AND p.deleted = 0
            """)
    ProjectDetailsVO selectProjectDetails(@Param("projectId") Long projectId);
}
