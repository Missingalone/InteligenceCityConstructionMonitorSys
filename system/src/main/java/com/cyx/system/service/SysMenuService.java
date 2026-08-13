package com.cyx.system.service;

import com.cyx.system.entity.dto.MenuSaveDTO;
import com.cyx.system.entity.vo.MenuVO;

import java.util.List;

/**
 * 菜单管理服务 — 维护前端路由和按钮权限。
 */
public interface SysMenuService {
    /**
     * 查询菜单平铺列表
     * */
    List<MenuVO> list();
    /**
     * 查询菜单树 — 将平铺菜单按 parent_id 组装成父子嵌套结构。
     */
    List<MenuVO> tree();
    /**
     * 新增或修改菜单
     * */
    Long save(MenuSaveDTO dto);
    /**
     * 删除菜单（有子节点则拒绝）
     * */
    void delete(Long id);
}
