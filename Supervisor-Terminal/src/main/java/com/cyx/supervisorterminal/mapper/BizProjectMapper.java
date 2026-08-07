package com.cyx.supervisorterminal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyx.supervisorterminal.entity.po.BizProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BizProjectMapper extends BaseMapper<BizProject> {

    @Select("""
            SELECT DISTINCT p.*
            FROM biz_project p
            JOIN biz_project_member pm ON pm.project_id = p.id
            JOIN sys_user u ON u.id = pm.user_id
            WHERE u.username = #{username} AND u.deleted = 0 AND p.deleted = 0
            ORDER BY p.id DESC
            """)
    List<BizProject> selectByMemberUsername(@Param("username") String username);

    @Select("""
            SELECT COUNT(1)
            FROM biz_project p
            JOIN biz_project_member pm ON pm.project_id = p.id
            JOIN sys_user u ON u.id = pm.user_id
            WHERE p.id = #{projectId} AND u.username = #{username}
              AND u.deleted = 0 AND p.deleted = 0
            """)
    long countAccessibleProject(@Param("projectId") Long projectId, @Param("username") String username);

    @Select("""
            SELECT COUNT(1)
            FROM biz_project p
            JOIN biz_project_member pm ON pm.project_id = p.id
            JOIN sys_user u ON u.id = pm.user_id
            WHERE p.enterprise_id = #{enterpriseId} AND u.username = #{username}
              AND u.deleted = 0 AND p.deleted = 0
            """)
    long countAccessibleEnterprise(@Param("enterpriseId") Long enterpriseId, @Param("username") String username);
}
