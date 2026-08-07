package com.cyx.supervisorterminal.entity.vo;

import lombok.Data;

@Data
public class EnterpriseVO {
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
}
