package com.cyx.system.service;

import com.cyx.system.entity.dto.MenuSaveDTO;
import com.cyx.system.entity.vo.MenuVO;

import java.util.List;

public interface SysMenuService {
    List<MenuVO> list();
    Long save(MenuSaveDTO dto);
    void delete(Long id);
}
