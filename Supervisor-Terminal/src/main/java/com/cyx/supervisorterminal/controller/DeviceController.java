package com.cyx.supervisorterminal.controller;

import com.cyx.result.Result;
import com.cyx.supervisorterminal.entity.dto.DeviceSaveDTO;
import com.cyx.supervisorterminal.entity.vo.DeviceVO;
import com.cyx.supervisorterminal.service.DeviceService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 监管端设备档案接口。 */
@RestController
@RequestMapping("/supervisor/devices")
public class DeviceController {
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    /** 查询当前账号可见设备。 */
    @GetMapping
    @PreAuthorize("hasAuthority('supervisor:device:list')")
    public Result<List<DeviceVO>> list() {
        return Result.success(deviceService.list());
    }

    /** 查询设备详情。 */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('supervisor:device:query')")
    public Result<DeviceVO> getById(@PathVariable Long id) {
        return Result.success(deviceService.getById(id));
    }

    /** 创建设备并绑定项目。 */
    @PostMapping
    @PreAuthorize("hasAuthority('supervisor:device:add')")
    public Result<Long> create(@Valid @RequestBody DeviceSaveDTO dto) {
        return Result.success("创建成功", deviceService.create(dto));
    }

    /** 修改设备档案。 */
    @PutMapping
    @PreAuthorize("hasAuthority('supervisor:device:edit')")
    public Result<Void> update(@Valid @RequestBody DeviceSaveDTO dto) {
        deviceService.update(dto);
        return Result.success("修改成功", null);
    }

    /** 逻辑删除设备。 */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('supervisor:device:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        deviceService.delete(id);
        return Result.success("删除成功", null);
    }
}
