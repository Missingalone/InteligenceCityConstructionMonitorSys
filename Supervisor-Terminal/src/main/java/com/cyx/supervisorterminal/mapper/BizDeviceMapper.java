package com.cyx.supervisorterminal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyx.supervisorterminal.entity.po.BizDevice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BizDeviceMapper extends BaseMapper<BizDevice> {

    @Select("""
            SELECT DISTINCT d.*
            FROM biz_device d
            JOIN biz_project_member pm ON pm.project_id = d.project_id
            JOIN sys_user u ON u.id = pm.user_id
            WHERE u.username = #{username} AND u.deleted = 0 AND d.deleted = 0
            ORDER BY d.id DESC
            """)
    List<BizDevice> selectByProjectMemberUsername(@Param("username") String username);
}
