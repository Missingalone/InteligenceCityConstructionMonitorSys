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
        SysUser user = requireUser(id);
        return toVO(user);
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
        vo.setRoleCodes(userMapper.selectRoleCodesByUserId(user.getId()));
        return vo;
    }
}
