package com.cyx.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cyx.exception.BusException;
import com.cyx.system.entity.dto.MenuSaveDTO;
import com.cyx.system.entity.po.SysMenu;
import com.cyx.system.entity.vo.MenuVO;
import com.cyx.system.mapper.SysMenuMapper;
import com.cyx.system.service.SysMenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单服务实现 — 支持平铺查询和树形结构返回。
 * <p>
 * 树形构建逻辑：先将所有菜单按 parent_id 分组，再递归组装子节点。
 * 时间复杂度 O(n)，适应菜单数量通常不超过 500 的场景。
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {

    private final SysMenuMapper menuMapper;

    public SysMenuServiceImpl(SysMenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    @Override
    public List<MenuVO> list() {
        return menuMapper.selectList(Wrappers.<SysMenu>lambdaQuery()
                        .orderByAsc(SysMenu::getSortOrder).orderByAsc(SysMenu::getId))
                .stream().map(this::toVO).toList();
    }

    @Override
    public List<MenuVO> tree() {
        // 查出所有菜单，按 parentId 分组
        List<SysMenu> all = menuMapper.selectList(Wrappers.<SysMenu>lambdaQuery()
                .orderByAsc(SysMenu::getSortOrder).orderByAsc(SysMenu::getId));
        Map<Long, List<MenuVO>> childrenMap = all.stream()
                .map(this::toVO)
                .collect(Collectors.groupingBy(vo -> vo.getParentId() != null ? vo.getParentId() : 0L));

        // 从根节点（parent_id = 0）开始递归组装
        List<MenuVO> roots = childrenMap.getOrDefault(0L, List.of());
        for (MenuVO root : roots) {
            buildChildren(root, childrenMap);
        }
        return roots;
    }

    /**
     * 递归填充子菜单，直到叶子节点。
     */
    private void buildChildren(MenuVO parent, Map<Long, List<MenuVO>> childrenMap) {
        List<MenuVO> children = childrenMap.getOrDefault(parent.getId(), List.of());
        if (!children.isEmpty()) {
            parent.setChildren(children);
            for (MenuVO child : children) {
                buildChildren(child, childrenMap);
            }
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public Long save(MenuSaveDTO dto) {
        SysMenu menu = dto.getId() == null ? new SysMenu() : requireMenu(dto.getId());
        BeanUtils.copyProperties(dto, menu, "createdAt", "updatedAt");
        if (menu.getId() == null) {
            menuMapper.insert(menu);
        } else {
            menuMapper.updateById(menu);
        }
        return menu.getId();
    }

    @Override
    public void delete(Long id) {
        // 有子菜单时不允许删除，防止产生孤儿节点
        long childCount = menuMapper.selectCount(Wrappers.<SysMenu>lambdaQuery().eq(SysMenu::getParentId, id));
        if (childCount > 0) {
            throw new BusException("当前菜单存在子节点，不能删除");
        }
        menuMapper.deleteById(requireMenu(id).getId());
    }

    private SysMenu requireMenu(Long id) {
        SysMenu menu = menuMapper.selectById(id);
        if (menu == null) {
            throw new BusException("菜单不存在");
        }
        return menu;
    }

    private MenuVO toVO(SysMenu menu) {
        MenuVO vo = new MenuVO();
        BeanUtils.copyProperties(menu, vo);
        return vo;
    }
}
