package com.cyx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyx.system.entity.po.SysOperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志 Mapper — 日志只增不删不查，查询接口走 Service 层的条件构造器。
 * 写操作由 {@link com.cyx.system.aspect.OperationLogAspect} 完成。
 */
@Mapper
public interface SysOperationLogMapper extends BaseMapper<SysOperationLog> {
}
