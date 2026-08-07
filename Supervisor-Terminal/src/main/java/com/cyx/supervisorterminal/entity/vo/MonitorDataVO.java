package com.cyx.supervisorterminal.entity.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 返回给监管端的设备监测记录。 */
@Data
public class MonitorDataVO {
    private Long id;
    private Long deviceId;
    private Long projectId;
    private BigDecimal pm25;
    private BigDecimal pm10;
    private BigDecimal noiseDb;
    private BigDecimal temperature;
    private BigDecimal humidity;
    private BigDecimal windSpeed;
    private String windDirection;
    private LocalDateTime collectedAt;
}
