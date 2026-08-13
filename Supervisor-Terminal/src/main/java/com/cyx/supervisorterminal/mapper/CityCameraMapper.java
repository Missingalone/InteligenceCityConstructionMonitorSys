package com.cyx.supervisorterminal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyx.supervisorterminal.entity.po.CityCamera;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface CityCameraMapper extends BaseMapper<CityCamera> {

    @Select("""
            SELECT DISTINCT c.*
            FROM city_camera c
            JOIN biz_project_member pm ON pm.project_id = c.project_id
            JOIN sys_user u ON u.id = pm.user_id
            WHERE u.username = #{username} AND u.deleted = 0 AND c.deleted = 0
            ORDER BY c.id DESC
            """)
    Page<CityCamera> selectAccessiblePage(Page<CityCamera> page, @Param("username") String username);
}
