package com.cyx.system.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作日志实体 — 对应 sys_operation_log 表，记录每次业务操作的审计信息。
 * <p>
 * 本表不使用逻辑删除，日志数据只增不删（通过定时任务归档历史数据）。
 */
@Data
@TableName("sys_operation_log")
public class SysOperationLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 操作人用户ID */
    private Long userId;
    /** 操作人用户名（冗余存储，即使用户被删也能追溯） */
    private String username;
    /** 操作模块名称 */
    private String moduleName;
    /** 操作名称 */
    private String operationName;
    /** 请求方法：GET/POST/PUT/DELETE */
    private String requestMethod;
    /** 请求URI */
    private String requestUri;
    /** 请求参数（超过1KB截断） */
    private String requestParams;
    /** 响应HTTP状态码 */
    private Integer responseCode;
    /** 客户端IP */
    private String clientIp;
    /** 接口执行耗时（毫秒） */
    private Long executionTimeMs;
    /** 操作时间 */
    private LocalDateTime createdAt;
}
