package com.cyx.system.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 字典项保存请求 — 必须指定所属字典类型。
 */
@Data
public class DictItemSaveDTO {
    private Long id;
    /** 所属字典类型ID，创建时必传 */
    @NotNull(message = "字典类型不能为空")
    private Long dictTypeId;
    @NotBlank(message = "字典项标签不能为空")
    private String itemLabel;
    @NotBlank(message = "字典项值不能为空")
    private String itemValue;
    private Integer sortOrder = 0;
    private Integer status = 1;
    private String remark;
}
