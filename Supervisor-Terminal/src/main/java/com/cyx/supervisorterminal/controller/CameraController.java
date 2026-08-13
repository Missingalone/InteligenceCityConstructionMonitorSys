package com.cyx.supervisorterminal.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyx.result.Result;
import com.cyx.supervisorterminal.entity.dto.CameraSaveDTO;
import com.cyx.supervisorterminal.entity.vo.CameraDetailsVO;
import com.cyx.supervisorterminal.entity.vo.CameraVO;
import com.cyx.supervisorterminal.service.CameraService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/supervisor/camera")
@RequiredArgsConstructor
public class CameraController {

    private final CameraService cameraService;

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('supervisor:device:list')")
    public Result<Page<CameraVO>> getCameraPage(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return Result.success(cameraService.cameraPage(pageNum, pageSize));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('supervisor:device:delete')")
    public Result<Void> deleteCamera(@PathVariable Long id) {
        cameraService.deleteCamera(id);
        return Result.success("删除成功", null);
    }
    @GetMapping("/details/{id}")
    @PreAuthorize("hasAuthority('supervisor:device:query')")
    public Result<CameraDetailsVO> getCameraDetails(@PathVariable Long id) {
        return Result.success(cameraService.getCameraDetails(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('supervisor:device:add')")
    public Result<Long> createCamera(@Valid @RequestBody CameraSaveDTO dto) {
        return Result.success("创建成功", cameraService.createCamera(dto));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('supervisor:device:edit')")
    public Result<Void> updateCamera(@Valid @RequestBody CameraSaveDTO dto) {
        cameraService.updateCamera(dto);
        return Result.success("修改成功", null);
    }
}
