package com.cyx.supervisorterminal.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CameraDetailsVO {

    private Long id;
    private String cameraCode;
    private String cameraName;
    private Long projectId;
    private Long foundationPitId;
    private String cameraType;
    private String deviceModel;
    private String manufacturer;
    private String installationAddress;
    private String direction;
    private Integer status;
    private Integer hasAudio;
    private Integer hasPtz;
    private String remark;
    private Long createdBy;
    private LocalDateTime createdTime;
    private Long updatedBy;
    private LocalDateTime updatedTime;

}
