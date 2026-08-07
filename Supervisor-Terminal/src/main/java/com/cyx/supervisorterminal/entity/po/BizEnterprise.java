package com.cyx.supervisorterminal.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("biz_enterprise")
public class BizEnterprise {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long organizationId;
    private String enterpriseName;
    private String unifiedSocialCreditCode;
    private String legalRepresentative;
    private String contactName;
    private String contactMobile;
    private String address;
    private String qualificationInfo;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @TableLogic
    private Integer deleted;
}
