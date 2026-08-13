package com.cyx.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cyx.exception.BusException;
import com.cyx.system.entity.dto.RoleMenuAssignDTO;
import com.cyx.system.entity.dto.RoleSaveDTO;
import com.cyx.system.entity.po.SysRole;
import com.cyx.system.entity.po.SysRoleMenu;
import com.cyx.system.entity.vo.RoleVO;
import com.cyx.system.mapper.SysRoleMapper;
import com.cyx.system.mapper.SysRoleMenuMapper;
import com.cyx.system.service.SysRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * 角色服务实现 — 角色 CRUD、菜单授权、角色详情（含已分配菜单ID）。
 * <p>
 * 菜单授权采用全量替换策略：前端传完整的菜单ID列表，服务端先删后增。
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    private final SysRoleMapper roleMapper;
    private final SysRoleMenuMapper roleMenuMapper;

    public SysRoleServiceImpl(SysRoleMapper roleMapper, SysRoleMenuMapper roleMenuMapper) {
        this.roleMapper = roleMapper;
        this.roleMenuMapper = roleMenuMapper;
    }

    @Override
    public List<RoleVO> list() {
        return roleMapper.selectList(Wrappers.<SysRole>lambdaQuery().orderByAsc(SysRole::getId))
                .stream().map(this::toVO).toList();
    }

    @Override
    public RoleVO getById(Long id) {
        RoleVO vo = toVO(requireRole(id));
        // 查询该角色已分配的菜单ID列表，前端初始化权限树时使用
        List<Long> menuIds = roleMenuMapper.selectList(
                Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getRoleId, id))
                .stream().map(SysRoleMenu::getMenuId).toList();
        vo.setMenuIds(menuIds);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(RoleSaveDTO dto) {
        SysRole role = dto.getId() == null ? new SysRole() : requireRole(dto.getId());
        long sameCode = roleMapper.selectCount(Wrappers.<SysRole>lambdaQuery()
                .eq(SysRole::getRoleCode, dto.getRoleCode())
                .ne(dto.getId() != null, SysRole::getId, dto.getId()));
        if (sameCode > 0) {
            throw new BusException("角色编码已存在");
        }
        BeanUtils.copyProperties(dto, role, "createdAt", "updatedAt");
        if (role.getId() == null) {
            roleMapper.insert(role);
        } else {
            roleMapper.updateById(role);
        }
        return role.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        requireRole(id);
        // 删除角色时级联删除角色-菜单关联
        roleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getRoleId, id));
        roleMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignMenus(RoleMenuAssignDTO dto) {
        requireRole(dto.getRoleId());
        // 全量替换：先清空旧权限，再插入新权限
        roleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getRoleId, dto.getRoleId()));
        for (Long menuId : dto.getMenuIds() == null ? Collections.<Long>emptyList() : dto.getMenuIds()) {
            SysRoleMenu relation = new SysRoleMenu();
            relation.setRoleId(dto.getRoleId());
            relation.setMenuId(menuId);
            roleMenuMapper.insert(relation);
        }
    }

    private SysRole requireRole(Long id) {
        SysRole role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusException("角色不存在");
        }
        return role;
    }

    private RoleVO toVO(SysRole role) {
        RoleVO vo = new RoleVO();
        BeanUtils.copyProperties(role, vo);
        return vo;
    }
}
