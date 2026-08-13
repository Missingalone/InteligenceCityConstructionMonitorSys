package com.cyx.supervisorterminal.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** 摄像头新增及修改参数。 */
@Data
public class CameraSaveDTO {
    private Long id;
    @NotBlank
    private String cameraCode;
    @NotBlank
    private String cameraName;
    @NotNull
    private Long projectId;
    private Long foundationPitId;
    private String cameraType;
    private String deviceModel;
    private String manufacturer;
    private String installationAddress;
    private String direction;
    private Integer status = 1;
    private Integer hasAudio = 0;
    private Integer hasPtz = 0;
    private String remark;
}
