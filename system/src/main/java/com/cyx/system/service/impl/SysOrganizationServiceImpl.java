package com.cyx.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cyx.exception.BusException;
import com.cyx.system.entity.dto.OrganizationSaveDTO;
import com.cyx.system.entity.po.SysOrganization;
import com.cyx.system.entity.vo.OrganizationVO;
import com.cyx.system.mapper.SysOrganizationMapper;
import com.cyx.system.service.SysOrganizationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysOrganizationServiceImpl implements SysOrganizationService {

    private final SysOrganizationMapper organizationMapper;

    public SysOrganizationServiceImpl(SysOrganizationMapper organizationMapper) {
        this.organizationMapper = organizationMapper;
    }

    @Override
    public List<OrganizationVO> list() {
        return organizationMapper.selectList(Wrappers.<SysOrganization>lambdaQuery()
                        .orderByAsc(SysOrganization::getSortOrder).orderByAsc(SysOrganization::getId))
                .stream().map(this::toVO).toList();
    }

    @Override
    public Long save(OrganizationSaveDTO dto) {
        SysOrganization organization = dto.getId() == null ? new SysOrganization() : requireOrganization(dto.getId());
        long sameCode = organizationMapper.selectCount(Wrappers.<SysOrganization>lambdaQuery()
                .eq(SysOrganization::getOrgCode, dto.getOrgCode())
                .ne(dto.getId() != null, SysOrganization::getId, dto.getId()));
        if (sameCode > 0) {
            throw new BusException("组织编码已存在");
        }
        BeanUtils.copyProperties(dto, organization, "createdAt", "updatedAt");
        if (organization.getId() == null) {
            organizationMapper.insert(organization);
        } else {
            organizationMapper.updateById(organization);
        }
        return organization.getId();
    }

    @Override
    public void delete(Long id) {
        long childCount = organizationMapper.selectCount(Wrappers.<SysOrganization>lambdaQuery()
                .eq(SysOrganization::getParentId, id));
        if (childCount > 0) {
            throw new BusException("当前组织存在下级组织，不能删除");
        }
        organizationMapper.deleteById(requireOrganization(id).getId());
    }

    private SysOrganization requireOrganization(Long id) {
        SysOrganization organization = organizationMapper.selectById(id);
        if (organization == null) {
            throw new BusException("组织不存在");
        }
        return organization;
    }

    private OrganizationVO toVO(SysOrganization organization) {
        OrganizationVO vo = new OrganizationVO();
        BeanUtils.copyProperties(organization, vo);
        return vo;
    }
}
