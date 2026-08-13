package com.cyx.supervisorterminal.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CameraVO {

    private Long id;
    private String cameraName;
    private String cameraCode;
    private String cameraType;
    private String deviceModel;
    private String manufacturer;
    private String installationAddress;
    private String direction;
    private Integer status;
    private String remark;
    private Long createdBy;
    private LocalDateTime createdTime;


}
