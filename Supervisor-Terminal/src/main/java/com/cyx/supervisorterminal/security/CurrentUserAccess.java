package com.cyx.supervisorterminal.security;

import com.cyx.exception.BusException;
import com.cyx.supervisorterminal.mapper.CurrentUserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserAccess {
    private final CurrentUserMapper currentUserMapper;

    public CurrentUserAccess(CurrentUserMapper currentUserMapper) {
        this.currentUserMapper = currentUserMapper;
    }

    /**
     * 判断当前账号是否拥有管理员全项目数据范围。
     */
    public boolean canAccessAllProjects() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }

    /**
     * 获取 JWT subject 中的当前登录用户名。
     */
    public String username() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new BusException("请先登录");
        }
        // JWT Filter 将 subject 设置为用户名，数据权限 SQL 用该用户名关联 sys_user。
        return authentication.getName();
    }

    /**
     * 获取当前有效系统用户编号，用于记录处理人和审核人。
     */
    public Long userId() {
        Long userId = currentUserMapper.selectIdByUsername(username());
        if (userId == null) {
            throw new BusException("当前用户不存在或已停用");
        }
        return userId;
    }
}
