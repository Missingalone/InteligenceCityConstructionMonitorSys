package com.cyx.system.controller;

import com.cyx.result.Result;
import com.cyx.system.entity.dto.DictItemSaveDTO;
import com.cyx.system.entity.dto.DictTypeSaveDTO;
import com.cyx.system.entity.vo.DictItemVO;
import com.cyx.system.entity.vo.DictTypeVO;
import com.cyx.system.service.SysDictService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 字典管理接口 — 维护系统中所有下拉选项、单选按钮等枚举数据。
 * <p>
 * 前端典型用法：
 * <ol>
 *   <li>列表页不需要字典项，调 {@code GET /system/dict-types}</li>
 *   <li>编辑页需要下拉选项，调 {@code GET /system/dict-types/{id}}</li>
 *   <li>其他模块按编码获取映射，调 {@code GET /system/dict-types/code/{code}/items}</li>
 * </ol>
 */
@RestController
@RequestMapping("/system/dict-types")
public class SysDictController {

    private final SysDictService dictService;

    public SysDictController(SysDictService dictService) {
        this.dictService = dictService;
    }

    /* ==================== 字典类型 ==================== */

    /** 查询全部字典类型（不含字典项，列表页使用） */
    @GetMapping
    @PreAuthorize("hasAuthority('system:dict:list')")
    public Result<List<DictTypeVO>> listTypes() {
        return Result.success(dictService.listTypes());
    }

    /** 查询字典类型详情（含字典项，编辑页使用） */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:dict:query')")
    public Result<DictTypeVO> getTypeById(@PathVariable Long id) {
        return Result.success(dictService.getTypeById(id));
    }

    /** 新增或修改字典类型（id 为空则新增，不为空则修改） */
    @PostMapping
    @PreAuthorize("hasAuthority('system:dict:edit')")
    public Result<Long> saveType(@Valid @RequestBody DictTypeSaveDTO dto) {
        return Result.success("保存成功", dictService.saveType(dto));
    }

    /** 删除字典类型（级联删除下属字典项） */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:dict:delete')")
    public Result<Void> deleteType(@PathVariable Long id) {
        dictService.deleteType(id);
        return Result.success("删除成功", null);
    }

    /* ==================== 字典项 ==================== */

    /** 查询指定字典类型下的字典项 */
    @GetMapping("/{dictTypeId}/items")
    @PreAuthorize("hasAuthority('system:dict:list')")
    public Result<List<DictItemVO>> listItems(@PathVariable Long dictTypeId) {
        return Result.success(dictService.listItems(dictTypeId));
    }

    /** 新增或修改字典项 */
    @PostMapping("/items")
    @PreAuthorize("hasAuthority('system:dict:edit')")
    public Result<Long> saveItem(@Valid @RequestBody DictItemSaveDTO dto) {
        return Result.success("保存成功", dictService.saveItem(dto));
    }

    /** 删除字典项 */
    @DeleteMapping("/items/{id}")
    @PreAuthorize("hasAuthority('system:dict:delete')")
    public Result<Void> deleteItem(@PathVariable Long id) {
        dictService.deleteItem(id);
        return Result.success("删除成功", null);
    }

    /* ==================== 按编码查询（供其他模块使用） ==================== */

    /**
     * 根据字典编码获取键值对映射。
     * 例如 {@code GET /system/dict-types/code/project_type/items}
     * 返回 {"MUNICIPAL": "市政工程", "BUILDING": "房屋建筑"}
     */
    @GetMapping("/code/{code}/items")
    @PreAuthorize("hasAuthority('system:dict:list')")
    public Result<Map<String, String>> getItemsByCode(@PathVariable String code) {
        return Result.success(dictService.getItemsByCode(code));
    }
}
