package com.cyx.system.service;

import com.cyx.system.entity.dto.RoleMenuAssignDTO;
import com.cyx.system.entity.dto.RoleSaveDTO;
import com.cyx.system.entity.vo.RoleVO;

import java.util.List;

/**
 * 角色管理服务 — 角色 CRUD 及菜单授权。
 */
public interface SysRoleService {
    /**
     * 查询全部角色
     */
    List<RoleVO> list();

    /**
     * 查询角色详情
     * （含已分配的菜单ID列表）
     *
     */
    RoleVO getById(Long id);

    /**
     * 新增或修改角色（id为空则新增）
     *
     */
    Long save(RoleSaveDTO dto);

    /**
     * 删除角色及角色-菜单关联
     */
    void delete(Long id);

    /**
     * 为角色分配菜单权限（全量替换）
     */
    void assignMenus(RoleMenuAssignDTO dto);
}
