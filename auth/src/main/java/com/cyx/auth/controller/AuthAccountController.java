package com.cyx.auth.controller;

import com.cyx.auth.entity.dto.PasswordChangeDTO;
import com.cyx.auth.service.AuthAccountService;
import com.cyx.result.Result;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 当前登录账号的安全操作接口。
 */
@RestController
@RequestMapping("/auth/account")
public class AuthAccountController {
    private final AuthAccountService accountService;

    public AuthAccountController(AuthAccountService accountService) {
        this.accountService = accountService;
    }

    /** 修改当前账号密码，必须携带有效 Bearer Token。 */
    @PutMapping("/password")
    public Result<Void> changePassword(@Valid @RequestBody PasswordChangeDTO dto) {
        accountService.changePassword(dto);
        return Result.success("密码修改成功，请使用新密码重新登录", null);
    }
}
