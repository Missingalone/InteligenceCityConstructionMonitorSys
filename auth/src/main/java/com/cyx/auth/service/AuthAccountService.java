package com.cyx.auth.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cyx.auth.entity.dto.PasswordChangeDTO;
import com.cyx.exception.BusException;
import com.cyx.system.entity.po.SysUser;
import com.cyx.system.mapper.SysUserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 登录账号安全服务。
 */
@Service
public class AuthAccountService {
    private final SysUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthAccountService(SysUserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 校验旧密码后更新当前用户密码，避免登录用户修改其他账号。
     */
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(PasswordChangeDTO dto) {
        String username = currentUsername();
        SysUser user = userMapper.selectOne(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getUsername, username));
        if (user == null || !Integer.valueOf(1).equals(user.getStatus())) {
            throw new BusException("当前账号不存在或已停用");
        }
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPasswordHash())) {
            throw new BusException("旧密码不正确");
        }
        if (passwordEncoder.matches(dto.getNewPassword(), user.getPasswordHash())) {
            throw new BusException("新密码不能与旧密码相同");
        }

        // 数据库只保存 BCrypt 散列值，不保存或记录明文密码。
        user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        userMapper.updateById(user);
    }

    /** 获取 JWT 过滤器写入的当前用户名。 */
    private String currentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new BusException("请先登录");
        }
        return authentication.getName();
    }
}
