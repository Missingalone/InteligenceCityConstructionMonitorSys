package com.cyx.publicterminal.service.impl;

import com.cyx.exception.BusException;
import com.cyx.publicterminal.entity.dto.PublicFeedbackDTO;
import com.cyx.publicterminal.entity.po.BizPublicFeedback;
import com.cyx.publicterminal.entity.vo.*;
import com.cyx.publicterminal.mapper.*;
import com.cyx.publicterminal.service.PublicPortalService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 公众门户实现，负责生成反馈编号并限制结果查询条件。
 */
@Service
public class PublicPortalServiceImpl implements PublicPortalService {
    private final PublicPortalMapper portalMapper;
    private final PublicFeedbackMapper feedbackMapper;

    public PublicPortalServiceImpl(PublicPortalMapper p, PublicFeedbackMapper f) {
        portalMapper = p;
        feedbackMapper = f;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PublicProjectVO> projects() {
        return portalMapper.selectProjects();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String submit(PublicFeedbackDTO dto) {
        BizPublicFeedback f = new BizPublicFeedback();
        BeanUtils.copyProperties(dto, f);
        f.setFeedbackNo("FB" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        f.setStatus("PENDING");
        feedbackMapper.insert(f);
        return f.getFeedbackNo();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PublicFeedbackVO feedback(String no, String mobile) {
        PublicFeedbackVO vo = portalMapper.selectFeedback(no, mobile);
        if (vo == null) throw new BusException("反馈不存在或手机号不匹配");
        return vo;
    }
}
