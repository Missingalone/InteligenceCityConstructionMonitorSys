package com.cyx.adminterminal.service;

import com.cyx.adminterminal.entity.dto.FeedbackHandleDTO;
import com.cyx.adminterminal.entity.vo.*;

import java.util.List;

/**
 * 管理员运营总览与反馈处理服务。
 */
public interface AdminOperationService {
    /**
     * 查询一期运营总览。
     */
    DashboardStatsVO dashboard();

    /**
     * 查询公众反馈。
     */
    List<AdminFeedbackVO> feedback();

    /**
     * 处理公众反馈。
     */
    void handleFeedback(Long id, FeedbackHandleDTO dto);
}
