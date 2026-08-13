package com.cyx.supervisorterminal.service;

import com.cyx.supervisorterminal.entity.dto.DeviceSaveDTO;
import com.cyx.supervisorterminal.entity.vo.DeviceVO;

import java.util.List;

/**
 * 监管端监测设备档案服务。
 */
public interface DeviceService {
    /**
     * 查询当前账号可见的设备。
     */
    List<DeviceVO> list();

    /**
     * 查询设备详情并校验项目数据权限。
     */
    DeviceVO getById(Long id);

    /**
     * 在有权访问的项目下创建设备。
     */
    Long create(DeviceSaveDTO dto);

    /**
     * 修改设备档案。
     */
    void update(DeviceSaveDTO dto);

    /**
     * 逻辑删除设备。
     */
    void delete(Long id);
}
