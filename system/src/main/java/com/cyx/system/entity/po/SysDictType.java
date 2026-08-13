package com.cyx.system.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 字典类型 — 对应 sys_dict_type 表，定义一类字典的编码和名称。
 * 例如 dict_code = "project_type"，可用于管理所有项目类型的下拉选项。
 */
@Data
@TableName("sys_dict_type")
public class SysDictType {
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 字典名称（中文展示） */
    private String dictName;
    /** 字典编码（英文标识，程序中引用时使用） */
    private String dictCode;
    /** 启用状态：1-启用 0-停用 */
    private Integer status;
    /** 备注 */
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @TableLogic
    private Integer deleted;
}
