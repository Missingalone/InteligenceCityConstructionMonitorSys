package com.cyx.supervisorterminal.service;

import com.cyx.supervisorterminal.entity.dto.AlarmHandleDTO;
import com.cyx.supervisorterminal.entity.dto.AlarmCloseDTO;
import com.cyx.supervisorterminal.entity.po.BizDevice;
import com.cyx.supervisorterminal.entity.po.BizDeviceMonitorData;
import com.cyx.supervisorterminal.entity.vo.AlarmRecordVO;

import java.util.List;

/**
 * 告警生成、查询和处理服务。
 */
public interface AlarmService {
    /**
     * 根据一条监测数据评估所有匹配规则并创建未重复的告警。
     */
    void evaluate(BizDevice device, BizDeviceMonitorData data);

    /**
     * 查询当前账号可见项目的告警。
     */
    List<AlarmRecordVO> list();

    /**
     * 处理指定告警。
     */
    void handle(Long id, AlarmHandleDTO dto);

    /**
     * 关闭已经解决的告警，作为告警生命周期的最终状态。
     */
    void close(Long id, AlarmCloseDTO dto);
}
