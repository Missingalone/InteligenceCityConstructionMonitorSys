package com.cyx.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyx.system.entity.po.SysOperationLog;
import com.cyx.system.entity.vo.OperationLogVO;
import com.cyx.system.mapper.SysOperationLogMapper;
import com.cyx.system.service.SysOperationLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 操作日志查询实现 — 只提供查询能力，不暴露修改删除接口。
 * <p>
 * 日志表的数据只增不减，通过定时任务归档过期数据。
 */
@Service
public class SysOperationLogServiceImpl implements SysOperationLogService {

    private final SysOperationLogMapper mapper;

    public SysOperationLogServiceImpl(SysOperationLogMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public IPage<OperationLogVO> page(int page, int size, String username, String module) {
        LambdaQueryWrapper<SysOperationLog> wrapper = new LambdaQueryWrapper<>();
        // 按用户名模糊筛选 — 用于安全审计时定位目标用户的操作记录
        if (username != null && !username.isBlank()) {
            wrapper.like(SysOperationLog::getUsername, username);
        }
        // 按模块名模糊筛选 — 缩小查询范围到特定业务模块
        if (module != null && !module.isBlank()) {
            wrapper.like(SysOperationLog::getModuleName, module);
        }
        wrapper.orderByDesc(SysOperationLog::getCreatedAt);

        IPage<SysOperationLog> pageResult = mapper.selectPage(new Page<>(page, size), wrapper);
        // 转换为 VO 返回，避免直接暴露数据库实体
        return pageResult.convert(this::toVO);
    }

    private OperationLogVO toVO(SysOperationLog log) {
        OperationLogVO vo = new OperationLogVO();
        BeanUtils.copyProperties(log, vo);
        return vo;
    }
}
