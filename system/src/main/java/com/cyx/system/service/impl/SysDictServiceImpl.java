package com.cyx.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cyx.exception.BusException;
import com.cyx.system.entity.dto.DictItemSaveDTO;
import com.cyx.system.entity.dto.DictTypeSaveDTO;
import com.cyx.system.entity.po.SysDictItem;
import com.cyx.system.entity.po.SysDictType;
import com.cyx.system.entity.vo.DictItemVO;
import com.cyx.system.entity.vo.DictTypeVO;
import com.cyx.system.mapper.SysDictItemMapper;
import com.cyx.system.mapper.SysDictTypeMapper;
import com.cyx.system.service.SysDictService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典服务实现。
 * <p>
 * 字典类型与字典项是一对多关系，删除类型时级联删除其下所有字典项。
 * 字典编码和字典项值均需做唯一性校验，防止数据混乱。
 */
@Service
public class SysDictServiceImpl implements SysDictService {

    private final SysDictTypeMapper dictTypeMapper;
    private final SysDictItemMapper dictItemMapper;

    public SysDictServiceImpl(SysDictTypeMapper dictTypeMapper, SysDictItemMapper dictItemMapper) {
        this.dictTypeMapper = dictTypeMapper;
        this.dictItemMapper = dictItemMapper;
    }

    /* ==================== 字典类型 ==================== */

    @Override
    public List<DictTypeVO> listTypes() {
        return dictTypeMapper.selectList(
                Wrappers.<SysDictType>lambdaQuery().orderByAsc(SysDictType::getId))
                .stream().map(this::toTypeVO).toList();
    }

    @Override
    public DictTypeVO getTypeById(Long id) {
        DictTypeVO vo = toTypeVO(requireType(id));
        // 查询详情时同时返回该类型下的所有字典项，前端可直接渲染下拉列表
        vo.setItems(listItems(id));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveType(DictTypeSaveDTO dto) {
        // 字典编码唯一性：创建时全表查重，修改时排除自身
        long sameCode = dictTypeMapper.selectCount(Wrappers.<SysDictType>lambdaQuery()
                .eq(SysDictType::getDictCode, dto.getDictCode())
                .ne(dto.getId() != null, SysDictType::getId, dto.getId()));
        if (sameCode > 0) {
            throw new BusException("字典编码已存在");
        }
        SysDictType type = dto.getId() == null ? new SysDictType() : requireType(dto.getId());
        BeanUtils.copyProperties(dto, type, "createdAt", "updatedAt");
        if (type.getId() == null) {
            dictTypeMapper.insert(type);
        } else {
            dictTypeMapper.updateById(type);
        }
        return type.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteType(Long id) {
        requireType(id);
        // 级联删除：先删字典项再删类型，避免孤儿数据
        dictItemMapper.delete(Wrappers.<SysDictItem>lambdaQuery()
                .eq(SysDictItem::getDictTypeId, id));
        dictTypeMapper.deleteById(id);
    }

    /* ==================== 字典项 ==================== */

    @Override
    public List<DictItemVO> listItems(Long dictTypeId) {
        return dictItemMapper.selectList(Wrappers.<SysDictItem>lambdaQuery()
                .eq(SysDictItem::getDictTypeId, dictTypeId)
                .orderByAsc(SysDictItem::getSortOrder).orderByAsc(SysDictItem::getId))
                .stream().map(this::toItemVO).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveItem(DictItemSaveDTO dto) {
        // 校验父类型存在
        requireType(dto.getDictTypeId());
        // 同一字典类型下 itemValue 必须唯一
        long sameValue = dictItemMapper.selectCount(Wrappers.<SysDictItem>lambdaQuery()
                .eq(SysDictItem::getDictTypeId, dto.getDictTypeId())
                .eq(SysDictItem::getItemValue, dto.getItemValue())
                .ne(dto.getId() != null, SysDictItem::getId, dto.getId()));
        if (sameValue > 0) {
            throw new BusException("字典项值已存在");
        }
        SysDictItem item = dto.getId() == null ? new SysDictItem() : requireItem(dto.getId());
        BeanUtils.copyProperties(dto, item, "createdAt", "updatedAt");
        if (item.getId() == null) {
            dictItemMapper.insert(item);
        } else {
            dictItemMapper.updateById(item);
        }
        return item.getId();
    }

    @Override
    public void deleteItem(Long id) {
        dictItemMapper.deleteById(requireItem(id).getId());
    }

    /* ==================== 对外查询 ==================== */

    @Override
    public Map<String, String> getItemsByCode(String dictCode) {
        // 先查类型ID，再查该项列表，组装成有序 LinkedHashMap 保证前端渲染顺序
        SysDictType type = dictTypeMapper.selectOne(Wrappers.<SysDictType>lambdaQuery()
                .eq(SysDictType::getDictCode, dictCode));
        if (type == null) {
            return Map.of();
        }
        List<SysDictItem> items = dictItemMapper.selectList(Wrappers.<SysDictItem>lambdaQuery()
                .eq(SysDictItem::getDictTypeId, type.getId())
                .eq(SysDictItem::getStatus, 1)
                .orderByAsc(SysDictItem::getSortOrder).orderByAsc(SysDictItem::getId));
        // LinkedHashMap 保证插入顺序 = 排序顺序，前端遍历时无需再排
        Map<String, String> result = new LinkedHashMap<>();
        for (SysDictItem item : items) {
            result.put(item.getItemValue(), item.getItemLabel());
        }
        return result;
    }

    /* ==================== 内部辅助 ==================== */

    private SysDictType requireType(Long id) {
        SysDictType type = dictTypeMapper.selectById(id);
        if (type == null) {
            throw new BusException("字典类型不存在");
        }
        return type;
    }

    private SysDictItem requireItem(Long id) {
        SysDictItem item = dictItemMapper.selectById(id);
        if (item == null) {
            throw new BusException("字典项不存在");
        }
        return item;
    }

    private DictTypeVO toTypeVO(SysDictType type) {
        DictTypeVO vo = new DictTypeVO();
        BeanUtils.copyProperties(type, vo);
        return vo;
    }

    private DictItemVO toItemVO(SysDictItem item) {
        DictItemVO vo = new DictItemVO();
        BeanUtils.copyProperties(item, vo);
        return vo;
    }
}
