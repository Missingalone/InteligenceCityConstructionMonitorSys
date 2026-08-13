package com.cyx.system.service;

import com.cyx.system.entity.dto.UserSaveDTO;
import com.cyx.system.entity.dto.UserUpdateDTO;
import com.cyx.system.entity.vo.UserVO;

import java.util.List;

/**
 * 用户管理服务 — 用户 CRUD、密码重置、状态启停。
 */
public interface SysUserService {
    /** 查询用户列表 */
    List<UserVO> list();
    /** 查询用户详情 */
    UserVO getById(Long id);
    /** 创建用户 */
    Long create(UserSaveDTO dto);
    /**
     *  修改用户信息 */
    void update(UserUpdateDTO dto);
    /**
     *  删除用户
     *  */
    void delete(Long id);
    /**
     * 重置用户密码 — 管理员操作，不需要旧密码。
     */
    void resetPassword(Long id, String newPassword);
    /**
     * 切换用户启用/停用状态 — 停用后用户无法登录。
     */
    void toggleStatus(Long id);
}
