package com.cyx.adminterminal.service.impl;

import com.cyx.exception.BusException;
import com.cyx.adminterminal.entity.dto.FeedbackHandleDTO;
import com.cyx.adminterminal.entity.vo.*;
import com.cyx.adminterminal.mapper.AdminOperationMapper;
import com.cyx.adminterminal.service.AdminOperationService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 管理员运营服务实现，处理动作记录当前 JWT 用户。
 */
@Service
public class AdminOperationServiceImpl implements AdminOperationService {
    private final AdminOperationMapper mapper;

    public AdminOperationServiceImpl(AdminOperationMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DashboardStatsVO dashboard() {
        return mapper.selectDashboard();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AdminFeedbackVO> feedback() {
        return mapper.selectFeedback();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleFeedback(Long id, FeedbackHandleDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (mapper.handleFeedback(id, dto.getHandleResult(), username) == 0)
            throw new BusException("反馈不存在或当前用户无效");
    }
}
