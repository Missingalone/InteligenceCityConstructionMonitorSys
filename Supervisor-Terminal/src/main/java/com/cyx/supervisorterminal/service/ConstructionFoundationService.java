package com.cyx.supervisorterminal.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyx.supervisorterminal.entity.dto.ConstructionUpdateDTO;
import com.cyx.supervisorterminal.entity.po.ConstructionFoundation;

public interface ConstructionFoundationService {

    /**
     * 获取分页数据
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<ConstructionFoundation> getConstructionFoundationPage(int pageNum, int pageSize);


    /**
     * 获取详情
     * @param id
     * @return
     */
    ConstructionFoundation getConstructionFoundationDetails(Long id);

    /**
     * 删除基坑
     * @param id
     * @return
     */
    void deleteConstructionFoundation(Long id);


    /**
     * 更新基坑信息
     * @return
     */
    Long createConstructionFoundation(ConstructionUpdateDTO dto);

    void updateConstructionFoundation(ConstructionUpdateDTO dto);
}
