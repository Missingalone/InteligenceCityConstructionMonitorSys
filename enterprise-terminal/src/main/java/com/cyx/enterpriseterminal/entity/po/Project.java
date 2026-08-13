package com.cyx.enterpriseterminal.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("biz_project")
public class Project implements Serializable {



    /**
     * 项目ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目编号
     */
    private String projectCode;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 建设单位ID
     */
    private Long enterpriseId;

    /**
     * 监理单位ID
     */
    private Long supervisorOrgId;

    /**
     * 项目类型
     */
    private String projectType;

    /**
     * 项目状态
     */
    private String projectStatus;

    /**
     * 项目地址
     */
    private String address;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 计划开始日期
     */
    private LocalDate plannedStartDate;

    /**
     * 计划结束日期
     */
    private LocalDate plannedEndDate;

    /**
     * 实际开始日期
     */
    private LocalDate actualStartDate;

    /**
     * 实际结束日期
     */
    private LocalDate actualEndDate;

    /**
     * 项目进度百分比
     */
    private BigDecimal progressPercent;

    /**
     * 项目负责人
     */
    private String projectManager;

    /**
     * 负责人手机号
     */
    private String managerMobile;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 逻辑删除：0未删除，1已删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;
}