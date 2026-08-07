package com.cyx.supervisorterminal.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeviceSaveDTO {
    private Long id;
    @NotBlank
    private String deviceCode;
    @NotBlank
    private String deviceName;
    @NotNull
    private Long projectId;
    @NotBlank
    private String deviceType;
    private String manufacturer;
    private String model;
    private String installationLocation;
    private String status = "OFFLINE";
    private LocalDateTime installedAt;
    private String remark;
}
