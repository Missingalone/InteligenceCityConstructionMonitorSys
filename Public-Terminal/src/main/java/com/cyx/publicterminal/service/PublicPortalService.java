package com.cyx.publicterminal.service;

import com.cyx.publicterminal.entity.dto.PublicFeedbackDTO;
import com.cyx.publicterminal.entity.vo.*;

import java.util.List;

/**
 * 公众项目查询和反馈服务。
 */
public interface PublicPortalService {
    /**
     * 查询公开项目。
     */
    List<PublicProjectVO> projects();

    /**
     * 提交公众反馈并返回反馈编号。
     */
    String submit(PublicFeedbackDTO dto);

    /**
     * 按反馈编号和手机号查询处理结果。
     */
    PublicFeedbackVO feedback(String no, String mobile);
}
