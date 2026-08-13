package com.cyx.supervisorterminal.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyx.exception.BusException;
import com.cyx.supervisorterminal.entity.dto.CameraSaveDTO;
import com.cyx.supervisorterminal.entity.po.CityCamera;
import com.cyx.supervisorterminal.entity.po.ConstructionFoundation;
import com.cyx.supervisorterminal.entity.vo.CameraDetailsVO;
import com.cyx.supervisorterminal.entity.vo.CameraVO;
import com.cyx.supervisorterminal.mapper.BizProjectMapper;
import com.cyx.supervisorterminal.mapper.CityCameraMapper;
import com.cyx.supervisorterminal.mapper.ConstructionFoundationMapper;
import com.cyx.supervisorterminal.security.CurrentUserAccess;
import com.cyx.supervisorterminal.service.CameraService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CameraServiceImpl implements CameraService {
    private final CityCameraMapper cameraMapper;
    private final ConstructionFoundationMapper foundationMapper;
    private final BizProjectMapper projectMapper;
    private final CurrentUserAccess access;

    public CameraServiceImpl(CityCameraMapper cameraMapper, ConstructionFoundationMapper foundationMapper,
                             BizProjectMapper projectMapper, CurrentUserAccess access) {
        this.cameraMapper = cameraMapper;
        this.foundationMapper = foundationMapper;
        this.projectMapper = projectMapper;
        this.access = access;
    }

    @Override
    public Page<CameraVO> cameraPage(int pageNum, int pageSize) {
        Page<CityCamera> query = new Page<>(Math.max(pageNum, 1), Math.min(Math.max(pageSize, 1), 100));
        Page<CityCamera> source = access.canAccessAllProjects()
                ? cameraMapper.selectPage(query, Wrappers.<CityCamera>lambdaQuery().orderByDesc(CityCamera::getId))
                : cameraMapper.selectAccessiblePage(query, access.username());
        Page<CameraVO> result = new Page<>(source.getCurrent(), source.getSize(), source.getTotal());
        result.setRecords(source.getRecords().stream().map(this::toListVO).toList());
        return result;
    }

    @Override
    public CameraDetailsVO getCameraDetails(Long id) {
        return toDetailsVO(requireAccessibleCamera(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCamera(CameraSaveDTO dto) {
        requireAccessibleProject(dto.getProjectId());
        validateFoundation(dto.getFoundationPitId(), dto.getProjectId());
        ensureCodeAvailable(dto.getCameraCode(), null);
        CityCamera camera = new CityCamera();
        BeanUtils.copyProperties(dto, camera, "id");
        camera.setCreatedBy(access.userId());
        camera.setUpdatedBy(access.userId());
        cameraMapper.insert(camera);
        return camera.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCamera(CameraSaveDTO dto) {
        if (dto.getId() == null) throw new BusException("摄像头ID不能为空");
        CityCamera camera = requireAccessibleCamera(dto.getId());
        requireAccessibleProject(dto.getProjectId());
        validateFoundation(dto.getFoundationPitId(), dto.getProjectId());
        ensureCodeAvailable(dto.getCameraCode(), dto.getId());
        BeanUtils.copyProperties(dto, camera, "createdBy", "createdTime", "deleted");
        camera.setUpdatedBy(access.userId());
        camera.setUpdatedTime(LocalDateTime.now());
        cameraMapper.updateById(camera);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCamera(Long id) {
        cameraMapper.deleteById(requireAccessibleCamera(id).getId());
    }

    private CityCamera requireAccessibleCamera(Long id) {
        CityCamera camera = cameraMapper.selectById(id);
        if (camera == null) throw new BusException("摄像头不存在");
        requireAccessibleProject(camera.getProjectId());
        return camera;
    }

    private void requireAccessibleProject(Long projectId) {
        if (projectId == null || projectMapper.selectById(projectId) == null) throw new BusException("项目不存在");
        if (!access.canAccessAllProjects()
                && projectMapper.countAccessibleProject(projectId, access.username()) == 0) {
            throw new BusException("无权访问该项目摄像头");
        }
    }

    private void validateFoundation(Long foundationId, Long projectId) {
        if (foundationId == null) return;
        ConstructionFoundation foundation = foundationMapper.selectById(foundationId);
        if (foundation == null) throw new BusException("关联基坑不存在");
        if (!projectId.equals(foundation.getProjectId())) throw new BusException("摄像头与基坑必须属于同一项目");
    }

    private void ensureCodeAvailable(String code, Long excludeId) {
        long count = cameraMapper.selectCount(Wrappers.<CityCamera>lambdaQuery()
                .eq(CityCamera::getCameraCode, code)
                .ne(excludeId != null, CityCamera::getId, excludeId));
        if (count > 0) throw new BusException("摄像头编号已存在");
    }

    private CameraVO toListVO(CityCamera camera) {
        CameraVO vo = new CameraVO();
        BeanUtils.copyProperties(camera, vo);
        return vo;
    }

    private CameraDetailsVO toDetailsVO(CityCamera camera) {
        CameraDetailsVO vo = new CameraDetailsVO();
        BeanUtils.copyProperties(camera, vo);
        return vo;
    }
}
