package com.cyx.supervisorterminal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyx.supervisorterminal.entity.po.BizDeviceMonitorData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BizDeviceMonitorDataMapper extends BaseMapper<BizDeviceMonitorData> {

    /** 查询当前用户有权查看的设备采样记录。 */
    @Select("""
            SELECT DISTINCT d.*
            FROM biz_device_monitor_data d
            JOIN biz_project_member pm ON pm.project_id = d.project_id
            JOIN sys_user u ON u.id = pm.user_id
            WHERE u.username = #{username} AND u.deleted = 0
            ORDER BY d.collected_at DESC, d.id DESC
            """)
    List<BizDeviceMonitorData> selectByProjectMemberUsername(@Param("username") String username);
}
