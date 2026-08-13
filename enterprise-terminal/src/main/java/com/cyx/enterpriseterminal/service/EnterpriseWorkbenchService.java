package com.cyx.enterpriseterminal.service;

import com.cyx.enterpriseterminal.entity.dto.*;
import com.cyx.enterpriseterminal.entity.vo.*;

import java.util.List;

/**
 * 施工企业项目、告警和整改工作台服务。
 */
public interface EnterpriseWorkbenchService {
    /**
     * 查询本企业项目。
     */
    List<EnterpriseProjectVO> projects();

    /**
     * 上报本企业项目进度。
     */
    void updateProgress(Long projectId, ProjectProgressDTO dto);

    /**
     * 查询本企业项目告警。
     */
    List<EnterpriseAlarmVO> alarms();

    /**
     * 查询本企业整改单。
     */
    List<EnterpriseRectificationVO> rectifications();

    /**
     * 提交整改结果。
     */
    void submitRectification(Long id, RectificationSubmitDTO dto);

}
