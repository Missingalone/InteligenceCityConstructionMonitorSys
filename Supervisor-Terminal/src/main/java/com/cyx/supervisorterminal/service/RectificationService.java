package com.cyx.supervisorterminal.service;

import com.cyx.supervisorterminal.entity.dto.*;
import com.cyx.supervisorterminal.entity.vo.RectificationOrderVO;

import java.util.List;

/**
 * 监管端整改通知下发与复查服务。
 */
public interface RectificationService {
    /**
     * 查询当前账号可见的整改单。
     */
    List<RectificationOrderVO> list();

    /**
     * 从告警下发整改通知单。
     */
    Long issue(RectificationIssueDTO dto);

    /**
     * 复查企业已提交的整改结果。
     */
    void review(Long id, RectificationReviewDTO dto);
}
