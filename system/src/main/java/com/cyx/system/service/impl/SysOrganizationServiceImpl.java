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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 组织服务实现 — 支持平铺查询和树形结构。
 * <p>
 * 组织编码唯一性校验覆盖新增和修改场景，树形组装逻辑与菜单树一致。
 */
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
    public List<OrganizationVO> tree() {
        List<SysOrganization> all = organizationMapper.selectList(Wrappers.<SysOrganization>lambdaQuery()
                .orderByAsc(SysOrganization::getSortOrder).orderByAsc(SysOrganization::getId));
        Map<Long, List<OrganizationVO>> childrenMap = all.stream()
                .map(this::toVO)
                .collect(Collectors.groupingBy(vo -> vo.getParentId() != null ? vo.getParentId() : 0L));

        List<OrganizationVO> roots = childrenMap.getOrDefault(0L, List.of());
        for (OrganizationVO root : roots) {
            buildChildren(root, childrenMap);
        }
        return roots;
    }

    /** 递归填充下级组织 */
    private void buildChildren(OrganizationVO parent, Map<Long, List<OrganizationVO>> childrenMap) {
        List<OrganizationVO> children = childrenMap.getOrDefault(parent.getId(), List.of());
        if (!children.isEmpty()) {
            parent.setChildren(children);
            for (OrganizationVO child : children) {
                buildChildren(child, childrenMap);
            }
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
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
        // 有子组织时不允许删除，防止树形结构断裂
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
