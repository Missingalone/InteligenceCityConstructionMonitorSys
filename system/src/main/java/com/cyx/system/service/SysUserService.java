package com.cyx.system.service;

import com.cyx.system.entity.dto.UserSaveDTO;
import com.cyx.system.entity.dto.UserUpdateDTO;
import com.cyx.system.entity.vo.UserVO;

import java.util.List;

public interface SysUserService {
    List<UserVO> list();
    UserVO getById(Long id);
    Long create(UserSaveDTO dto);
    void update(UserUpdateDTO dto);
    void delete(Long id);
}
