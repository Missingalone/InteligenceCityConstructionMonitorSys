package com.cyx.supervisorterminal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyx.supervisorterminal.entity.po.BizRectificationOrder;
import org.apache.ibatis.annotations.*;
import java.util.List;

/** 整改通知单数据访问。 */
@Mapper
public interface BizRectificationOrderMapper extends BaseMapper<BizRectificationOrder> {
    /** 查询当前监管人员负责项目下的整改单。 */
    @Select("""
            SELECT DISTINCT r.* FROM biz_rectification_order r
            JOIN biz_project_member pm ON pm.project_id = r.project_id
            JOIN sys_user u ON u.id = pm.user_id
            WHERE u.username = #{username} AND u.deleted = 0 AND r.deleted = 0
            ORDER BY r.issued_at DESC, r.id DESC
            """)
    List<BizRectificationOrder> selectByProjectMemberUsername(@Param("username") String username);
}
