package com.cyx.supervisorterminal.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/** 智慧城市施工监控摄像头。 */
@Data
@TableName("city_camera")
public class CityCamera {

    @TableId(type = IdType.AUTO)
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

    @TableLogic
    private Integer deleted;
}
