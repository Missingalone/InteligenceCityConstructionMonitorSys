package com.cyx.system.controller;

import com.cyx.result.Result;
import com.cyx.system.entity.dto.UserSaveDTO;
import com.cyx.system.entity.dto.UserUpdateDTO;
import com.cyx.system.entity.vo.UserVO;
import com.cyx.system.service.SysUserService;
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

@RestController
@RequestMapping("/system/users")
public class SysUserController {

    private final SysUserService userService;

    public SysUserController(SysUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('system:user:list')")
    public Result<List<UserVO>> list() {
        return Result.success(userService.list());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:user:query')")
    public Result<UserVO> getById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('system:user:add')")
    public Result<Long> create(@Valid @RequestBody UserSaveDTO dto) {
        return Result.success("创建成功", userService.create(dto));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Result<Void> update(@Valid @RequestBody UserUpdateDTO dto) {
        userService.update(dto);
        return Result.success("修改成功", null);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:user:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.success("删除成功", null);
    }
}
