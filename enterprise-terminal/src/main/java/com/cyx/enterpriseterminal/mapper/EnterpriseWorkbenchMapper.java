package com.cyx.enterpriseterminal.mapper;

import com.cyx.enterpriseterminal.entity.vo.*;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 施工企业工作台数据访问，所有语句均按当前用户名限定企业范围。
 */
@Mapper
public interface EnterpriseWorkbenchMapper {
    /**
     * 查询当前企业项目。
     */
    @Select("""
            SELECT p.* FROM biz_project p JOIN biz_enterprise e ON e.id=p.enterprise_id
            JOIN sys_user u ON u.organization_id=e.organization_id
            WHERE u.username=#{username} AND u.deleted=0 AND e.deleted=0 AND p.deleted=0 ORDER BY p.id DESC
            """)
    List<EnterpriseProjectVO> selectProjects(@Param("username") String username);

    /**
     * 在企业归属校验通过时更新项目进度。
     */
    @Update("""
            UPDATE biz_project p JOIN biz_enterprise e ON e.id=p.enterprise_id
            JOIN sys_user u ON u.organization_id=e.organization_id
            SET p.progress_percent=#{progress},p.actual_start_date=#{actualStartDate}
            WHERE p.id=#{projectId} AND u.username=#{username} AND u.deleted=0 AND p.deleted=0
            """)
    int updateProgress(@Param("projectId") Long projectId, @Param("progress") BigDecimal progress, @Param("actualStartDate") LocalDate actualStartDate, @Param("username") String username);

    /**
     * 查询当前企业项目告警。
     */
    @Select("""
            SELECT a.* FROM biz_alarm_record a JOIN biz_project p ON p.id=a.project_id
            JOIN biz_enterprise e ON e.id=p.enterprise_id JOIN sys_user u ON u.organization_id=e.organization_id
            WHERE u.username=#{username} AND u.deleted=0 AND a.deleted=0 ORDER BY a.triggered_at DESC
            """)
    List<EnterpriseAlarmVO> selectAlarms(@Param("username") String username);

    /**
     * 查询当前企业整改单。
     */
    @Select("""
            SELECT r.* FROM biz_rectification_order r JOIN biz_enterprise e ON e.id=r.enterprise_id
            JOIN sys_user u ON u.organization_id=e.organization_id
            WHERE u.username=#{username} AND u.deleted=0 AND r.deleted=0 ORDER BY r.issued_at DESC
            """)
    List<EnterpriseRectificationVO> selectRectifications(@Param("username") String username);

    /**
     * 仅允许所属企业提交待整改或被驳回的整改单。
     */
    @Update("""
            UPDATE biz_rectification_order r JOIN biz_enterprise e ON e.id=r.enterprise_id
            JOIN sys_user u ON u.organization_id=e.organization_id
            SET r.status='SUBMITTED',r.submitted_by=u.id,r.submitted_at=NOW(),
                r.result_description=#{description},r.evidence_urls=#{evidence}
            WHERE r.id=#{id} AND u.username=#{username} AND u.deleted=0 AND r.deleted=0
              AND r.status IN ('PENDING','REJECTED')
            """)
    int submitRectification(@Param("id") Long id, @Param("description") String description, @Param("evidence") String evidence, @Param("username") String username);
}
