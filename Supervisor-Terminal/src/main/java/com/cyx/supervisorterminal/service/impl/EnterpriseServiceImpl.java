package com.cyx.supervisorterminal.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cyx.exception.BusException;
import com.cyx.supervisorterminal.entity.dto.EnterpriseSaveDTO;
import com.cyx.supervisorterminal.entity.po.BizEnterprise;
import com.cyx.supervisorterminal.entity.po.BizProject;
import com.cyx.supervisorterminal.entity.vo.EnterpriseVO;
import com.cyx.supervisorterminal.mapper.BizEnterpriseMapper;
import com.cyx.supervisorterminal.mapper.BizProjectMapper;
import com.cyx.supervisorterminal.security.CurrentUserAccess;
import com.cyx.supervisorterminal.service.EnterpriseService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** 施工企业档案服务实现，负责信用代码唯一性和项目引用保护。 */
@Service
public class EnterpriseServiceImpl implements EnterpriseService {
    private final BizEnterpriseMapper enterpriseMapper;
    private final BizProjectMapper projectMapper;
    private final CurrentUserAccess currentUserAccess;

    public EnterpriseServiceImpl(BizEnterpriseMapper enterpriseMapper, BizProjectMapper projectMapper,
                                 CurrentUserAccess currentUserAccess) {
        this.enterpriseMapper = enterpriseMapper;
        this.projectMapper = projectMapper;
        this.currentUserAccess = currentUserAccess;
    }

    /** {@inheritDoc} */
    @Override
    public List<EnterpriseVO> list() {
        List<BizEnterprise> enterprises = currentUserAccess.canAccessAllProjects()
                ? enterpriseMapper.selectList(Wrappers.<BizEnterprise>lambdaQuery().orderByDesc(BizEnterprise::getId))
                : enterpriseMapper.selectByProjectMemberUsername(currentUserAccess.username());
        // 企业权限从项目成员关系推导，监管人员不会看到无关施工企业。
        return enterprises
                .stream().map(this::toVO).toList();
    }

    /** {@inheritDoc} */
    @Override
    public EnterpriseVO getById(Long id) {
        return toVO(requireAccessibleEnterprise(id));
    }

    /** {@inheritDoc} */
    @Override
    public Long create(EnterpriseSaveDTO dto) {
        ensureCreditCodeAvailable(dto.getUnifiedSocialCreditCode(), null);
        BizEnterprise enterprise = new BizEnterprise();
        BeanUtils.copyProperties(dto, enterprise, "id");
        enterpriseMapper.insert(enterprise);
        return enterprise.getId();
    }

    /** {@inheritDoc} */
    @Override
    public void update(EnterpriseSaveDTO dto) {
        if (dto.getId() == null) {
            throw new BusException("企业编号不能为空");
        }
        BizEnterprise enterprise = requireAccessibleEnterprise(dto.getId());
        ensureCreditCodeAvailable(dto.getUnifiedSocialCreditCode(), enterprise.getId());
        BeanUtils.copyProperties(dto, enterprise, "createdAt", "updatedAt", "deleted");
        enterpriseMapper.updateById(enterprise);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        requireAccessibleEnterprise(id);
        // 企业仍被项目引用时保留档案，避免项目详情出现无效的 enterprise_id。
        long projectCount = projectMapper.selectCount(Wrappers.<BizProject>lambdaQuery()
                .eq(BizProject::getEnterpriseId, id));
        if (projectCount > 0) {
            throw new BusException("该企业已关联项目，不能删除");
        }
        enterpriseMapper.deleteById(id);
    }

    private BizEnterprise requireEnterprise(Long id) {
        BizEnterprise enterprise = enterpriseMapper.selectById(id);
        if (enterprise == null) {
            throw new BusException("施工企业不存在");
        }
        return enterprise;
    }

    private BizEnterprise requireAccessibleEnterprise(Long id) {
        BizEnterprise enterprise = requireEnterprise(id);
        if (!currentUserAccess.canAccessAllProjects()
                && projectMapper.countAccessibleEnterprise(id, currentUserAccess.username()) == 0) {
            throw new BusException("无权访问该施工企业");
        }
        return enterprise;
    }

    private void ensureCreditCodeAvailable(String creditCode, Long excludeId) {
        if (creditCode == null || creditCode.isBlank()) {
            return;
        }
        // 更新时排除当前企业自身，创建时 excludeId 为 null。
        long count = enterpriseMapper.selectCount(Wrappers.<BizEnterprise>lambdaQuery()
                .eq(BizEnterprise::getUnifiedSocialCreditCode, creditCode)
                .ne(excludeId != null, BizEnterprise::getId, excludeId));
        if (count > 0) {
            throw new BusException("统一社会信用代码已存在");
        }
    }

    private EnterpriseVO toVO(BizEnterprise enterprise) {
        EnterpriseVO vo = new EnterpriseVO();
        BeanUtils.copyProperties(enterprise, vo);
        return vo;
    }
}
