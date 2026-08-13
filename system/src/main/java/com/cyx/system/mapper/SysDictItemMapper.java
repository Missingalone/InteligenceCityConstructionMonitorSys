package com.cyx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyx.system.entity.po.SysDictItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典项 Mapper — 字典项与标签值唯一性约束（dict_type_id, item_value）
 * 由 Service 层通过条件查询保证。
 */
@Mapper
public interface SysDictItemMapper extends BaseMapper<SysDictItem> {
}
