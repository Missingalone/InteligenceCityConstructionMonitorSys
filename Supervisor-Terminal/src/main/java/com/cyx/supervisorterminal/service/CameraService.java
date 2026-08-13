package com.cyx.supervisorterminal.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyx.supervisorterminal.entity.dto.CameraSaveDTO;
import com.cyx.supervisorterminal.entity.vo.CameraDetailsVO;
import com.cyx.supervisorterminal.entity.vo.CameraVO;

public interface CameraService {

    Page<CameraVO> cameraPage(int pageNum, int pageSize);

    CameraDetailsVO getCameraDetails(Long id);

    Long createCamera(CameraSaveDTO dto);

    void updateCamera(CameraSaveDTO dto);

    void deleteCamera(Long id);

}
