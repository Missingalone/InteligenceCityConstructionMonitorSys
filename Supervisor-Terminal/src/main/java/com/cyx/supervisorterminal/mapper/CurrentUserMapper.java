package com.cyx.supervisorterminal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/** 为业务审计字段提供当前系统用户编号。 */
@Mapper
public interface CurrentUserMapper {
    /** 根据 JWT 中的用户名查询有效用户编号。 */
    @Select("SELECT id FROM sys_user WHERE username = #{username} AND deleted = 0 LIMIT 1")
    Long selectIdByUsername(@Param("username") String username);
}
