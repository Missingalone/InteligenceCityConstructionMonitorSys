package com.cyx.system.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作日志返回对象 — 前端查询展示用。
 */
@Data
public class OperationLogVO {
    private Long id;
    private Long userId;
    private String username;
    private String moduleName;
    private String operationName;
    private String requestMethod;
    private String requestUri;
    private String requestParams;
    private Integer responseCode;
    private String clientIp;
    private Long executionTimeMs;
    private LocalDateTime createdAt;
}
