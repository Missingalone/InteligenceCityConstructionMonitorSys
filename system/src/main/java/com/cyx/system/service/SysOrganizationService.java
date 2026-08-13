package com.cyx.system.service;

import com.cyx.system.entity.dto.OrganizationSaveDTO;
import com.cyx.system.entity.vo.OrganizationVO;

import java.util.List;

/**
 * 组织管理服务 — 维护机构树形结构。
 */
public interface SysOrganizationService {
    /** 查询组织平铺列表 */
    List<OrganizationVO> list();
    /**
     * 查询组织树 — 按 parent_id 构建层级结构。
     */
    List<OrganizationVO> tree();
    /** 新增或修改组织 */
    Long save(OrganizationSaveDTO dto);
    /** 删除组织（有子组织则拒绝） */
    void delete(Long id);
}
