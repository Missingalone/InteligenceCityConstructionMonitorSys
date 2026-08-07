package com.cyx.supervisorterminal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyx.supervisorterminal.entity.po.BizAlarmRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/** 告警记录数据访问。 */
@Mapper
public interface BizAlarmRecordMapper extends BaseMapper<BizAlarmRecord> {

    /** 查询当前监管人员负责项目下的告警。 */
    @Select("""
            SELECT DISTINCT a.*
            FROM biz_alarm_record a
            JOIN biz_project_member pm ON pm.project_id = a.project_id
            JOIN sys_user u ON u.id = pm.user_id
            WHERE u.username = #{username} AND u.deleted = 0 AND a.deleted = 0
            ORDER BY a.triggered_at DESC, a.id DESC
            """)
    List<BizAlarmRecord> selectByProjectMemberUsername(@Param("username") String username);
}
