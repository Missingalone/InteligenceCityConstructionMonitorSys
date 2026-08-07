package com.cyx.supervisorterminal.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeviceVO {
    private Long id;
    private String deviceCode;
    private String deviceName;
    private Long projectId;
    private String deviceType;
    private String manufacturer;
    private String model;
    private String installationLocation;
    private String status;
    private LocalDateTime lastOnlineAt;
    private LocalDateTime installedAt;
    private String remark;
}
