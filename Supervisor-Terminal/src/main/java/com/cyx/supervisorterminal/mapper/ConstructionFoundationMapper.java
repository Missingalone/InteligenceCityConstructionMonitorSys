package com.cyx.supervisorterminal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyx.supervisorterminal.entity.po.ConstructionFoundation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ConstructionFoundationMapper extends BaseMapper<ConstructionFoundation> {

    @Select("""
            SELECT DISTINCT p.*
            FROM construction_foundation_pit p
            JOIN biz_project_member pm ON pm.project_id = p.project_id
            JOIN sys_user u ON u.id = pm.user_id
            WHERE u.username = #{username} AND u.deleted = 0 AND p.deleted = 0
            ORDER BY p.id DESC
            """)
    Page<ConstructionFoundation> selectAccessiblePage(Page<ConstructionFoundation> page,
                                                       @Param("username") String username);
}
