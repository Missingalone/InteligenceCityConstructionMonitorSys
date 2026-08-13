package com.cyx.system.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * 字典类型返回对象 — 前端展示用，不包含 deleted 和完整时间戳。
 */
@Data
public class DictTypeVO {
    private Long id;
    private String dictName;
    private String dictCode;
    private Integer status;
    private String remark;
    /** 下属字典项 — 仅在查询详情时填充，列表查询时为 null */
    private List<DictItemVO> items;
}
