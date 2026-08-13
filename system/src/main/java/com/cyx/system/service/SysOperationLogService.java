package com.cyx.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cyx.system.entity.vo.OperationLogVO;

/**
 * 操作日志查询服务 — 仅支持分页查询，不支持修改和删除。
 * 日志数据由 {@link com.cyx.system.aspect.OperationLogAspect} 自动写入。
 */
public interface SysOperationLogService {

    /**
     * 分页查询操作日志，支持按用户名和模块名筛选。
     *
     * @param page     页码（从1开始）
     * @param size     每页条数
     * @param username 操作人用户名（模糊匹配，可选）
     * @param module   模块名称（模糊匹配，可选）
     * @return 分页结果
     */
    IPage<OperationLogVO> page(int page, int size, String username, String module);
}
