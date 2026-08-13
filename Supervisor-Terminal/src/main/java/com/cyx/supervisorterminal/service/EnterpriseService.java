package com.cyx.supervisorterminal.service;

import com.cyx.supervisorterminal.entity.dto.EnterpriseSaveDTO;
import com.cyx.supervisorterminal.entity.vo.EnterpriseVO;

import java.util.List;

/**
 * 监管端施工企业档案服务。
 */
public interface EnterpriseService {
    /**
     * 查询当前账号可见的施工企业。
     */
    List<EnterpriseVO> list();

    /**
     * 查询施工企业详情。
     */
    EnterpriseVO getById(Long id);

    /**
     * 创建施工企业档案。
     */
    Long create(EnterpriseSaveDTO dto);

    /**
     * 修改施工企业档案。
     */
    void update(EnterpriseSaveDTO dto);

    /**
     * 删除未关联项目的施工企业。
     */
    void delete(Long id);
}
