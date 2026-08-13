package com.cyx.system.entity.vo;

import lombok.Data;

/**
 * 字典项返回对象 — 在字典类型详情中嵌套返回。
 */
@Data
public class DictItemVO {
    private Long id;
    private Long dictTypeId;
    private String itemLabel;
    private String itemValue;
    private Integer sortOrder;
    private Integer status;
    private String remark;
}
