package com.cyx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyx.system.entity.po.SysDictType;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典类型 Mapper — MyBatis-Plus BaseMapper 提供基础 CRUD，
 * 字典编码唯一性校验在 Service 层通过 Wrappers 条件查询实现。
 */
@Mapper
public interface SysDictTypeMapper extends BaseMapper<SysDictType> {
}
