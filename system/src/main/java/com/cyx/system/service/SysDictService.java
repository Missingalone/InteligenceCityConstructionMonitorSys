package com.cyx.system.service;

import com.cyx.system.entity.dto.DictItemSaveDTO;
import com.cyx.system.entity.dto.DictTypeSaveDTO;
import com.cyx.system.entity.vo.DictItemVO;
import com.cyx.system.entity.vo.DictTypeVO;

import java.util.List;
import java.util.Map;

/**
 * 字典管理服务接口 — 管理字典类型和字典项。
 * <p>
 * 业务规则：
 * <ul>
 *   <li>字典编码全局唯一，创建/修改时校验</li>
 *   <li>同一字典类型下 item_value 唯一</li>
 *   <li>删除字典类型时级联删除其下所有字典项</li>
 *   <li>对外暴露 {@code getItemsByCode} 供其他模块按编码拉取字典映射</li>
 * </ul>
 */
public interface SysDictService {

    /* ==================== 字典类型 ==================== */

    /** 查询全部字典类型（不含字典项） */
    List<DictTypeVO> listTypes();

    /** 查询单个字典类型及其下属字典项 */
    DictTypeVO getTypeById(Long id);

    /** 创建或修改字典类型 */
    Long saveType(DictTypeSaveDTO dto);

    /** 删除字典类型及其下属字典项 */
    void deleteType(Long id);

    /* ==================== 字典项 ==================== */

    /** 查询指定字典类型下全部字典项 */
    List<DictItemVO> listItems(Long dictTypeId);

    /** 创建或修改字典项 */
    Long saveItem(DictItemSaveDTO dto);

    /** 删除字典项 */
    void deleteItem(Long id);

    /* ==================== 对外查询 ==================== */

    /**
     * 根据字典编码获取键值对映射，供其他模块在业务逻辑中使用。
     * 例如 {@code getItemsByCode("project_type")} 返回 {"MUNICIPAL": "市政工程", ...}
     */
    Map<String, String> getItemsByCode(String dictCode);
}
