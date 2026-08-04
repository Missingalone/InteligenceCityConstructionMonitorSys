package com.cyx.system.service;

import com.cyx.system.entity.dto.RoleMenuAssignDTO;
import com.cyx.system.entity.dto.RoleSaveDTO;
import com.cyx.system.entity.vo.RoleVO;

import java.util.List;

public interface SysRoleService {
    List<RoleVO> list();
    Long save(RoleSaveDTO dto);
    void delete(Long id);
    void assignMenus(RoleMenuAssignDTO dto);
}
