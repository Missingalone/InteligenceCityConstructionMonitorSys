package com.cyx.publicterminal.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 公众反馈数据库对象。
 */
@Data
@TableName("biz_public_feedback")
public class BizPublicFeedback {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String feedbackNo;
    private Long projectId;
    private String feedbackType;
    private String content;
    private String contactName;
    private String contactMobile;
    private String attachmentUrls;
    private String status;
    private Long handlerId;
    private LocalDateTime handledAt;
    private String handleResult;
    @TableLogic
    private Integer deleted;
}
