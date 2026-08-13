package com.cyx.system.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 字典项 — 对应 sys_dict_item 表，属于某个字典类型的键值对。
 * 例如 dict_type_id=1 的 item_label="市政工程" item_value="MUNICIPAL"。
 */
@Data
@TableName("sys_dict_item")
public class SysDictItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 所属字典类型ID */
    private Long dictTypeId;
    /** 字典项标签（前端显示文本） */
    private String itemLabel;
    /** 字典项值（后端存储值） */
    private String itemValue;
    /** 排序序号 */
    private Integer sortOrder;
    /** 启用状态：1-启用 0-停用 */
    private Integer status;
    /** 备注 */
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @TableLogic
    private Integer deleted;
}
