package com.cyx.supervisorterminal.service;

import com.cyx.supervisorterminal.entity.dto.AlarmRuleSaveDTO;
import com.cyx.supervisorterminal.entity.vo.AlarmRuleVO;

import java.util.List;

/**
 * 告警规则配置服务。
 */
public interface AlarmRuleService {
    /**
     * 查询全部有效规则。
     */
    List<AlarmRuleVO> list();

    /**
     * 创建规则。
     */
    Long create(AlarmRuleSaveDTO dto);

    /**
     * 修改规则。
     */
    void update(AlarmRuleSaveDTO dto);

    /**
     * 逻辑删除规则。
     */
    void delete(Long id);
}
