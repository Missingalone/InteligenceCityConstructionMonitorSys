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
    public Long save(RoleSaveDTO dto) {
        SysRole role = dto.getId() == null ? new SysRole() : requireRole(dto.getId());
        long sameCode = roleMapper.selectCount(Wrappers.<SysRole>lambdaQuery()
                .eq(SysRole::getRoleCode, dto.getRoleCode()).ne(dto.getId() != null, SysRole::getId, dto.getId()));
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
        roleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getRoleId, id));
        roleMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignMenus(RoleMenuAssignDTO dto) {
        requireRole(dto.getRoleId());
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
