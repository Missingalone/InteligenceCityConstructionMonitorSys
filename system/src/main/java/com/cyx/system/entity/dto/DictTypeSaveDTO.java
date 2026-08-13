package com.cyx.system.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 字典类型保存请求 — 创建和修改共用，id 为空时新增，不为空时修改。
 */
@Data
public class DictTypeSaveDTO {
    /** 修改时必传，创建时留空 */
    private Long id;
    @NotBlank(message = "字典名称不能为空")
    private String dictName;
    @NotBlank(message = "字典编码不能为空")
    private String dictCode;
    private Integer status = 1;
    private String remark;
}
