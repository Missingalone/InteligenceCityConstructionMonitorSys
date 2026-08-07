package com.cyx.supervisorterminal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyx.supervisorterminal.entity.po.BizEnterprise;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BizEnterpriseMapper extends BaseMapper<BizEnterprise> {

    @Select("""
            SELECT DISTINCT e.*
            FROM biz_enterprise e
            JOIN biz_project p ON p.enterprise_id = e.id AND p.deleted = 0
            JOIN biz_project_member pm ON pm.project_id = p.id
            JOIN sys_user u ON u.id = pm.user_id
            WHERE u.username = #{username} AND u.deleted = 0 AND e.deleted = 0
            ORDER BY e.id DESC
            """)
    List<BizEnterprise> selectByProjectMemberUsername(@Param("username") String username);
}
