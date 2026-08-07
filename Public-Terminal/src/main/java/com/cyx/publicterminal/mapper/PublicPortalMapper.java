package com.cyx.publicterminal.mapper;

import com.cyx.publicterminal.entity.vo.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 公众门户只读查询。
 */
@Mapper
public interface PublicPortalMapper {
    /**
     * 查询允许公开的项目字段。
     */
    @Select("SELECT id,project_name,project_type,project_status,address,progress_percent FROM biz_project WHERE deleted=0 AND project_status<>'CLOSED' ORDER BY id DESC")
    List<PublicProjectVO> selectProjects();

    /**
     * 使用反馈编号和手机号双重校验查询结果。
     */
    @Select("SELECT feedback_no,feedback_type,content,status,handled_at,handle_result FROM biz_public_feedback WHERE feedback_no=#{no} AND contact_mobile=#{mobile} AND deleted=0 LIMIT 1")
    PublicFeedbackVO selectFeedback(@Param("no") String no, @Param("mobile") String mobile);
}
