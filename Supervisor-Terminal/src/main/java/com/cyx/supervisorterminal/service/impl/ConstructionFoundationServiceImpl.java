package com.cyx.supervisorterminal.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyx.exception.BusException;
import com.cyx.supervisorterminal.entity.dto.ConstructionUpdateDTO;
import com.cyx.supervisorterminal.entity.po.ConstructionFoundation;
import com.cyx.supervisorterminal.mapper.BizProjectMapper;
import com.cyx.supervisorterminal.mapper.ConstructionFoundationMapper;
import com.cyx.supervisorterminal.security.CurrentUserAccess;
import com.cyx.supervisorterminal.service.ConstructionFoundationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ConstructionFoundationServiceImpl implements ConstructionFoundationService {
    private final ConstructionFoundationMapper foundationMapper;
    private final BizProjectMapper projectMapper;
    private final CurrentUserAccess access;

    public ConstructionFoundationServiceImpl(ConstructionFoundationMapper foundationMapper,
                                              BizProjectMapper projectMapper, CurrentUserAccess access) {
        this.foundationMapper = foundationMapper;
        this.projectMapper = projectMapper;
        this.access = access;
    }

    @Override
    public Page<ConstructionFoundation> getConstructionFoundationPage(int pageNum, int pageSize) {
        Page<ConstructionFoundation> page = new Page<>(Math.max(pageNum, 1), Math.min(Math.max(pageSize, 1), 100));
        return access.canAccessAllProjects()
                ? foundationMapper.selectPage(page, Wrappers.<ConstructionFoundation>lambdaQuery()
                .orderByDesc(ConstructionFoundation::getId))
                : foundationMapper.selectAccessiblePage(page, access.username());
    }

    @Override
    public ConstructionFoundation getConstructionFoundationDetails(Long id) {
        return requireAccessibleFoundation(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createConstructionFoundation(ConstructionUpdateDTO dto) {
        requireAccessibleProject(dto.getProjectId());
        ensureCodeAvailable(dto.getPitCode(), null);
        ConstructionFoundation foundation = new ConstructionFoundation();
        BeanUtils.copyProperties(dto, foundation, "id");
        foundation.setCreatedBy(access.userId());
        foundation.setUpdatedBy(access.userId());
        foundationMapper.insert(foundation);
        return foundation.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateConstructionFoundation(ConstructionUpdateDTO dto) {
        if (dto.getId() == null) throw new BusException("基坑ID不能为空");
        ConstructionFoundation foundation = requireAccessibleFoundation(dto.getId());
        requireAccessibleProject(dto.getProjectId());
        ensureCodeAvailable(dto.getPitCode(), dto.getId());
        BeanUtils.copyProperties(dto, foundation, "createdBy", "createdTime", "deleted");
        foundation.setUpdatedBy(access.userId());
        foundation.setUpdatedTime(LocalDateTime.now());
        foundationMapper.updateById(foundation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteConstructionFoundation(Long id) {
        foundationMapper.deleteById(requireAccessibleFoundation(id).getId());
    }

    private ConstructionFoundation requireAccessibleFoundation(Long id) {
        ConstructionFoundation foundation = foundationMapper.selectById(id);
        if (foundation == null) throw new BusException("基坑不存在");
        requireAccessibleProject(foundation.getProjectId());
        return foundation;
    }

    private void requireAccessibleProject(Long projectId) {
        if (projectId == null || projectMapper.selectById(projectId) == null) throw new BusException("项目不存在");
        if (!access.canAccessAllProjects()
                && projectMapper.countAccessibleProject(projectId, access.username()) == 0) {
            throw new BusException("无权访问该项目基坑");
        }
    }

    private void ensureCodeAvailable(String pitCode, Long excludeId) {
        long count = foundationMapper.selectCount(Wrappers.<ConstructionFoundation>lambdaQuery()
                .eq(ConstructionFoundation::getPitCode, pitCode)
                .ne(excludeId != null, ConstructionFoundation::getId, excludeId));
        if (count > 0) throw new BusException("基坑编号已存在");
    }
}
