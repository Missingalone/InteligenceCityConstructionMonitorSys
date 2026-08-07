package com.cyx.auth.common_security;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cyx.system.entity.po.SysUser;
import com.cyx.system.mapper.SysUserMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final SysUserMapper userMapper;

    public UserDetailsService(SysUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        // 登录时只读取未被逻辑删除的用户；MyBatis-Plus 会自动拼接 deleted = 0。
        SysUser user = userMapper.selectOne(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getUsername, username));
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        if (!Integer.valueOf(1).equals(user.getStatus())) {
            throw new UsernameNotFoundException("账号已被禁用");
        }

        Set<SimpleGrantedAuthority> authorities = new LinkedHashSet<>();
        // 角色供 hasRole 使用，Spring Security 约定角色授权必须以 ROLE_ 开头。
        userMapper.selectRoleCodesByUserId(user.getId()).forEach(roleCode ->
                authorities.add(new SimpleGrantedAuthority("ROLE_" + roleCode)));
        // 菜单按钮权限供 hasAuthority("模块:资源:动作") 直接校验。
        userMapper.selectPermissionCodesByUserId(user.getId()).forEach(permissionCode ->
                authorities.add(new SimpleGrantedAuthority(permissionCode)));

        return User.withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(authorities)
                .build();
    }
}
