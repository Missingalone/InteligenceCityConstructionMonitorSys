package com.cyx.supervisorterminal.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("biz_device_monitor_data")
public class BizDeviceMonitorData {
    @TableId(type = IdType.AUTO)
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
    private String rawData;
    private LocalDateTime collectedAt;
    private LocalDateTime createdAt;
}
