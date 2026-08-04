package com.cyx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyx.system.entity.po.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    @Select("SELECT r.role_code FROM sys_role r "
            + "INNER JOIN sys_user_role ur ON ur.role_id = r.id "
            + "WHERE ur.user_id = #{userId} AND r.deleted = 0 AND r.status = 1")
    List<String> selectRoleCodesByUserId(@Param("userId") Long userId);

    @Select("SELECT m.permission_code FROM sys_menu m "
            + "INNER JOIN sys_role_menu rm ON rm.menu_id = m.id "
            + "INNER JOIN sys_user_role ur ON ur.role_id = rm.role_id "
            + "WHERE ur.user_id = #{userId} AND m.deleted = 0 AND m.status = 1 "
            + "AND m.permission_code IS NOT NULL AND m.permission_code <> ''")
    List<String> selectPermissionCodesByUserId(@Param("userId") Long userId);
}
