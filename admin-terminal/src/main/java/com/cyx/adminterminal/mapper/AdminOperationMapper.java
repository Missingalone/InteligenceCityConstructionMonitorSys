package com.cyx.adminterminal.mapper;
import com.cyx.adminterminal.entity.vo.*; import org.apache.ibatis.annotations.*; import java.util.List;
/** 管理员运营总览和反馈处理数据访问。 */
@Mapper public interface AdminOperationMapper {
    /** 聚合一期核心业务数量。 */
    @Select("""
      SELECT (SELECT COUNT(*) FROM biz_project WHERE deleted=0) project_count,
      (SELECT COUNT(*) FROM biz_device WHERE deleted=0 AND status='ONLINE') online_device_count,
      (SELECT COUNT(*) FROM biz_alarm_record WHERE deleted=0 AND alarm_status IN ('PENDING','HANDLING')) pending_alarm_count,
      (SELECT COUNT(*) FROM biz_rectification_order WHERE deleted=0 AND status IN ('PENDING','SUBMITTED','REJECTED')) active_rectification_count,
      (SELECT COUNT(*) FROM biz_public_feedback WHERE deleted=0 AND status='PENDING') pending_feedback_count
      """)
    DashboardStatsVO selectDashboard();
    /** 查询全部公众反馈。 */ @Select("SELECT * FROM biz_public_feedback WHERE deleted=0 ORDER BY id DESC") List<AdminFeedbackVO> selectFeedback();
    /** 处理反馈并通过用户名写入处理人。 */
    @Update("""
            UPDATE biz_public_feedback f JOIN sys_user u ON u.username=#{username} AND u.deleted=0
            SET f.status='RESOLVED',f.handler_id=u.id,f.handled_at=NOW(),f.handle_result=#{result}
            WHERE f.id=#{id} AND f.deleted=0
            """)
    int handleFeedback(@Param("id") Long id,@Param("result") String result,@Param("username") String username);
}
