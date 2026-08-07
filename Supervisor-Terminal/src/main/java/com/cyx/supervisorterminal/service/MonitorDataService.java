package com.cyx.supervisorterminal.service;

import com.cyx.supervisorterminal.entity.dto.MonitorDataReportDTO;
import com.cyx.supervisorterminal.entity.vo.MonitorDataVO;

import java.util.List;

/** 监测设备数据上报与监管查询服务。 */
public interface MonitorDataService {
    /**
     * 保存一条设备采样并刷新设备在线状态。
     *
     * @param dto 设备上报的采样数据
     * @return 新增监测记录编号
     */
    Long report(MonitorDataReportDTO dto);

    /**
     * 查询当前登录用户可见的监测记录。
     *
     * @return 按采集时间倒序排列的记录
     */
    List<MonitorDataVO> list();
}
