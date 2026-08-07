package com.cyx.publicterminal.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 面向公众公开的非敏感项目信息。
 */
@Data
public class PublicProjectVO {
    private Long id;
    private String projectName;
    private String projectType;
    private String projectStatus;
    private String address;
    private BigDecimal progressPercent;
}
