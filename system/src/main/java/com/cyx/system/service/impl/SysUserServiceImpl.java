package com.cyx.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cyx.exception.BusException;
import com.cyx.system.entity.dto.UserSaveDTO;
import com.cyx.system.entity.dto.UserUpdateDTO;
import com.cyx.system.entity.po.SysUser;
import com.cyx.system.entity.po.SysUserRole;
import com.cyx.system.entity.vo.UserVO;
import com.cyx.system.mapper.SysUserMapper;
import com.cyx.system.mapper.SysUserRoleMapper;
import com.cyx.system.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * 用户服务实现 — 用户 CRUD、角色分配、密码重置、状态管理。
 * <p>
 * 密码使用 BCrypt 存储，重置时直接替换散列值而非修改明文。
 * 状态为停用的用户 Spring Security 会拒绝认证（需配合 UserDetailsService）。
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper userMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public SysUserServiceImpl(SysUserMapper userMapper, SysUserRoleMapper userRoleMapper) {
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
    }

    @Override
    public List<UserVO> list() {
        return userMapper.selectList(Wrappers.<SysUser>lambdaQuery().orderByDesc(SysUser::getId))
                .stream().map(this::toVO).toList();
    }

    @Override
    public UserVO getById(Long id) {
        return toVO(requireUser(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(UserSaveDTO dto) {
        long count = userMapper.selectCount(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getUsername, dto.getUsername()));
        if (count > 0) {
            throw new BusException("用户名已存在");
        }
        SysUser user = new SysUser();
        BeanUtils.copyProperties(dto, user);
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        userMapper.insert(user);
        replaceRoles(user.getId(), dto.getRoleIds());
        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserUpdateDTO dto) {
        SysUser user = requireUser(dto.getId());
        // 保护字段：username、password 不通过 update 接口修改，避免误操作
        BeanUtils.copyProperties(dto, user, "username", "password", "passwordHash", "roleIds", "createdAt", "updatedAt");
        userMapper.updateById(user);
        if (dto.getRoleIds() != null) {
            replaceRoles(user.getId(), dto.getRoleIds());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        requireUser(id);
        userRoleMapper.delete(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, id));
        userMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long id, String newPassword) {
        SysUser user = requireUser(id);
        if (newPassword == null || newPassword.length() < 8 || newPassword.length() > 64) {
            throw new BusException("密码长度必须为8到64位");
        }
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleStatus(Long id) {
        SysUser user = requireUser(id);
        // 0→1 或 1→0，使用位运算翻转
        user.setStatus(user.getStatus() == 1 ? 0 : 1);
        userMapper.updateById(user);
    }

    /** 全量替换用户角色 — 先删后增 */
    private void replaceRoles(Long userId, List<Long> roleIds) {
        userRoleMapper.delete(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userId));
        for (Long roleId : roleIds == null ? Collections.<Long>emptyList() : roleIds) {
            SysUserRole relation = new SysUserRole();
            relation.setUserId(userId);
            relation.setRoleId(roleId);
            userRoleMapper.insert(relation);
        }
    }

    private SysUser requireUser(Long id) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            throw new BusException("用户不存在");
        }
        return user;
    }

    private UserVO toVO(SysUser user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        // 补充角色编码列表，前端用于展示和权限判断
        vo.setRoleCodes(userMapper.selectRoleCodesByUserId(user.getId()));
        return vo;
    }
}
