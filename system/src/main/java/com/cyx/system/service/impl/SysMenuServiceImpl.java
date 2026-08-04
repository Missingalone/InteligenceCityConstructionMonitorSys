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

import java.util.List;

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
