package com.cyx.enterpriseterminal.service.impl;

import com.cyx.exception.BusException;
import com.cyx.enterpriseterminal.entity.dto.*;
import com.cyx.enterpriseterminal.entity.vo.*;
import com.cyx.enterpriseterminal.mapper.EnterpriseWorkbenchMapper;
import com.cyx.enterpriseterminal.service.EnterpriseWorkbenchService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 企业工作台实现，通过 JWT 用户名在每条 SQL 中落实企业数据隔离。
 */
@Service
public class EnterpriseWorkbenchServiceImpl implements EnterpriseWorkbenchService {
    private final EnterpriseWorkbenchMapper mapper;

    public EnterpriseWorkbenchServiceImpl(EnterpriseWorkbenchMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EnterpriseProjectVO> projects() {
        return mapper.selectProjects(username());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateProgress(Long id, ProjectProgressDTO dto) {
        if (mapper.updateProgress(id, dto.getProgressPercent(), dto.getActualStartDate(), username()) == 0)
            throw new BusException("项目不存在或无权操作");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EnterpriseAlarmVO> alarms() {
        return mapper.selectAlarms(username());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EnterpriseRectificationVO> rectifications() {
        return mapper.selectRectifications(username());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void submitRectification(Long id, RectificationSubmitDTO dto) {
        if (mapper.submitRectification(id, dto.getResultDescription(), dto.getEvidenceUrls(), username()) == 0)
            throw new BusException("整改单不存在、无权操作或状态不允许提交");
    }

    /**
     * 获取 JWT Filter 写入安全上下文的用户名。
     */
    private String username() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
