package com.cyx.system.service;

import com.cyx.system.entity.dto.OrganizationSaveDTO;
import com.cyx.system.entity.vo.OrganizationVO;

import java.util.List;

public interface SysOrganizationService {
    List<OrganizationVO> list();
    Long save(OrganizationSaveDTO dto);
    void delete(Long id);
}
