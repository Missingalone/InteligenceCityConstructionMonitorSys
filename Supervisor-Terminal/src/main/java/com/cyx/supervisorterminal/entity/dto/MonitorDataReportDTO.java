package com.cyx.supervisorterminal.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 设备或网关向平台提交的一次环境监测采样。 */
@Data
public class MonitorDataReportDTO {
    @NotBlank
    private String deviceCode;
    private BigDecimal pm25;
    private BigDecimal pm10;
    private BigDecimal noiseDb;
    private BigDecimal temperature;
    private BigDecimal humidity;
    private BigDecimal windSpeed;
    private String windDirection;
    private String rawData;
    private LocalDateTime collectedAt;
}
