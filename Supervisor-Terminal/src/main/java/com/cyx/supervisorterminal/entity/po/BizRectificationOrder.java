package com.cyx.supervisorterminal.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/** 告警整改通知单及其审核状态。 */
@Data
@TableName("biz_rectification_order")
public class BizRectificationOrder {
    @TableId(type = IdType.AUTO) private Long id;
    private String orderNo; private Long projectId; private Long alarmId; private Long enterpriseId;
    private String title; private String content; private LocalDateTime deadlineAt; private String status;
    private Long issuedBy; private LocalDateTime issuedAt; private Long submittedBy; private LocalDateTime submittedAt;
    private String resultDescription; private String evidenceUrls; private Long reviewedBy;
    private LocalDateTime reviewedAt; private String reviewRemark;
    @TableLogic private Integer deleted;
}
