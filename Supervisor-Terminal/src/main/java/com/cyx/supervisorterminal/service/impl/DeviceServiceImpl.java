package com.cyx.supervisorterminal.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cyx.exception.BusException;
import com.cyx.supervisorterminal.entity.dto.DeviceSaveDTO;
import com.cyx.supervisorterminal.entity.po.BizDevice;
import com.cyx.supervisorterminal.entity.po.BizProject;
import com.cyx.supervisorterminal.entity.vo.DeviceVO;
import com.cyx.supervisorterminal.mapper.BizDeviceMapper;
import com.cyx.supervisorterminal.mapper.BizProjectMapper;
import com.cyx.supervisorterminal.security.CurrentUserAccess;
import com.cyx.supervisorterminal.service.DeviceService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** 设备档案服务实现，统一执行项目数据权限和设备编码唯一性校验。 */
@Service
public class DeviceServiceImpl implements DeviceService {
    private final BizDeviceMapper deviceMapper;
    private final BizProjectMapper projectMapper;
    private final CurrentUserAccess currentUserAccess;

    public DeviceServiceImpl(BizDeviceMapper deviceMapper, BizProjectMapper projectMapper,
                             CurrentUserAccess currentUserAccess) {
        this.deviceMapper = deviceMapper;
        this.projectMapper = projectMapper;
        this.currentUserAccess = currentUserAccess;
    }

    /** {@inheritDoc} */
    @Override
    public List<DeviceVO> list() {
        List<BizDevice> devices = currentUserAccess.canAccessAllProjects()
                ? deviceMapper.selectList(Wrappers.<BizDevice>lambdaQuery().orderByDesc(BizDevice::getId))
                : deviceMapper.selectByProjectMemberUsername(currentUserAccess.username());
        return devices.stream().map(this::toVO).toList();
    }

    /** {@inheritDoc} */
    @Override
    public DeviceVO getById(Long id) {
        return toVO(requireAccessibleDevice(id));
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(DeviceSaveDTO dto) {
        requireAccessibleProject(dto.getProjectId());
        ensureDeviceCodeAvailable(dto.getDeviceCode(), null);
        BizDevice device = new BizDevice();
        BeanUtils.copyProperties(dto, device, "id", "lastOnlineAt");
        deviceMapper.insert(device);
        return device.getId();
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DeviceSaveDTO dto) {
        if (dto.getId() == null) {
            throw new BusException("设备编号不能为空");
        }
        BizDevice device = requireAccessibleDevice(dto.getId());
        requireAccessibleProject(dto.getProjectId());
        ensureDeviceCodeAvailable(dto.getDeviceCode(), device.getId());
        // 上线时间只能由后续设备上报接口刷新，人工维护设备资料时不能覆盖它。
        BeanUtils.copyProperties(dto, device, "lastOnlineAt", "createdAt", "updatedAt", "deleted");
        deviceMapper.updateById(device);
    }

    /** {@inheritDoc} */
    @Override
    public void delete(Long id) {
        deviceMapper.deleteById(requireAccessibleDevice(id).getId());
    }

    private BizDevice requireAccessibleDevice(Long id) {
        BizDevice device = deviceMapper.selectById(id);
        if (device == null) {
            throw new BusException("设备不存在");
        }
        requireAccessibleProject(device.getProjectId());
        return device;
    }

    private void requireAccessibleProject(Long projectId) {
        BizProject project = projectMapper.selectById(projectId);
        if (project == null) {
            throw new BusException("项目不存在");
        }
        // 非管理员必须是项目成员，不能凭知道 projectId 操作其他项目设备。
        if (!currentUserAccess.canAccessAllProjects()
                && projectMapper.countAccessibleProject(projectId, currentUserAccess.username()) == 0) {
            throw new BusException("无权访问该项目设备");
        }
    }

    private void ensureDeviceCodeAvailable(String deviceCode, Long excludeId) {
        long count = deviceMapper.selectCount(Wrappers.<BizDevice>lambdaQuery()
                .eq(BizDevice::getDeviceCode, deviceCode)
                .ne(excludeId != null, BizDevice::getId, excludeId));
        if (count > 0) {
            throw new BusException("设备编码已存在");
        }
    }

    private DeviceVO toVO(BizDevice device) {
        DeviceVO vo = new DeviceVO();
        BeanUtils.copyProperties(device, vo);
        return vo;
    }
}
