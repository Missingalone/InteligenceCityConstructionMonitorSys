package com.cyx.supervisorterminal.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EnterpriseSaveDTO {
    private Long id;
    private Long organizationId;
    @NotBlank
    private String enterpriseName;
    private String unifiedSocialCreditCode;
    private String legalRepresentative;
    private String contactName;
    private String contactMobile;
    private String address;
    private String qualificationInfo;
    private Integer status = 1;
}
