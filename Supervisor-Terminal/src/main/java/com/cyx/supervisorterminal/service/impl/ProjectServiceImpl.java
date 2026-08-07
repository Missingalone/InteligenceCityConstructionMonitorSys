package com.cyx.supervisorterminal.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cyx.exception.BusException;
import com.cyx.supervisorterminal.entity.dto.ProjectMemberAssignDTO;
import com.cyx.supervisorterminal.entity.dto.ProjectSaveDTO;
import com.cyx.supervisorterminal.entity.po.BizEnterprise;
import com.cyx.supervisorterminal.entity.po.BizProject;
import com.cyx.supervisorterminal.entity.po.BizProjectMember;
import com.cyx.supervisorterminal.entity.vo.ProjectMemberVO;
import com.cyx.supervisorterminal.entity.vo.ProjectVO;
import com.cyx.supervisorterminal.mapper.BizEnterpriseMapper;
import com.cyx.supervisorterminal.mapper.BizProjectMapper;
import com.cyx.supervisorterminal.mapper.BizProjectMemberMapper;
import com.cyx.supervisorterminal.security.CurrentUserAccess;
import com.cyx.supervisorterminal.service.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/** 项目服务实现，负责项目成员数据范围、企业关联和成员全量分配。 */
@Service
public class ProjectServiceImpl implements ProjectService {
    private final BizProjectMapper projectMapper;
    private final BizEnterpriseMapper enterpriseMapper;
    private final BizProjectMemberMapper projectMemberMapper;
    private final CurrentUserAccess currentUserAccess;

    public ProjectServiceImpl(BizProjectMapper projectMapper, BizEnterpriseMapper enterpriseMapper,
                              BizProjectMemberMapper projectMemberMapper, CurrentUserAccess currentUserAccess) {
        this.projectMapper = projectMapper;
        this.enterpriseMapper = enterpriseMapper;
        this.projectMemberMapper = projectMemberMapper;
        this.currentUserAccess = currentUserAccess;
    }

    /** {@inheritDoc} */
    @Override
    public List<ProjectVO> list() {
        List<BizProject> projects = currentUserAccess.canAccessAllProjects()
                ? projectMapper.selectList(Wrappers.<BizProject>lambdaQuery().orderByDesc(BizProject::getId))
                : projectMapper.selectByMemberUsername(currentUserAccess.username());
        // ADMIN 不受项目成员限制；监管人员仅能看到自己在 biz_project_member 中的项目。
        return projects
                .stream().map(project -> toVO(project, false)).toList();
    }

    /** {@inheritDoc} */
    @Override
    public ProjectVO getById(Long id) {
        return toVO(requireAccessibleProject(id), true);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(ProjectSaveDTO dto) {
        // 项目必须关联已存在的施工企业，避免产生悬空外键业务数据。
        requireEnterprise(dto.getEnterpriseId());
        ensureProjectCodeAvailable(dto.getProjectCode(), null);
        BizProject project = new BizProject();
        BeanUtils.copyProperties(dto, project, "id");
        projectMapper.insert(project);
        return project.getId();
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ProjectSaveDTO dto) {
        if (dto.getId() == null) {
            throw new BusException("项目编号不能为空");
        }
        BizProject project = requireAccessibleProject(dto.getId());
        requireEnterprise(dto.getEnterpriseId());
        ensureProjectCodeAvailable(dto.getProjectCode(), project.getId());
        BeanUtils.copyProperties(dto, project, "createdAt", "updatedAt", "deleted");
        projectMapper.updateById(project);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        requireAccessibleProject(id);
        // 项目成员是项目的附属关系，删除项目时一并清理关联记录。
        projectMemberMapper.delete(Wrappers.<BizProjectMember>lambdaQuery().eq(BizProjectMember::getProjectId, id));
        projectMapper.deleteById(id);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replaceMembers(Long projectId, List<ProjectMemberAssignDTO> members) {
        requireAccessibleProject(projectId);
        // 成员分配按整份列表覆盖，调用方传空列表即可清空现有成员。
        projectMemberMapper.delete(Wrappers.<BizProjectMember>lambdaQuery()
                .eq(BizProjectMember::getProjectId, projectId));
        for (ProjectMemberAssignDTO dto : members == null ? Collections.<ProjectMemberAssignDTO>emptyList() : members) {
            BizProjectMember member = new BizProjectMember();
            member.setProjectId(projectId);
            member.setUserId(dto.getUserId());
            member.setMemberRole(dto.getMemberRole());
            projectMemberMapper.insert(member);
        }
    }

    private BizProject requireProject(Long id) {
        BizProject project = projectMapper.selectById(id);
        if (project == null) {
            throw new BusException("项目不存在");
        }
        return project;
    }

    private BizProject requireAccessibleProject(Long id) {
        BizProject project = requireProject(id);
        if (!currentUserAccess.canAccessAllProjects()
                && projectMapper.countAccessibleProject(id, currentUserAccess.username()) == 0) {
            throw new BusException("无权访问该项目");
        }
        return project;
    }

    private void requireEnterprise(Long id) {
        if (enterpriseMapper.selectById(id) == null) {
            throw new BusException("施工企业不存在");
        }
        // 非管理员只能把项目关联到自己已负责项目中的企业，不能借修改接口跨企业操作。
        if (!currentUserAccess.canAccessAllProjects()
                && projectMapper.countAccessibleEnterprise(id, currentUserAccess.username()) == 0) {
            throw new BusException("无权使用该施工企业");
        }
    }

    private void ensureProjectCodeAvailable(String projectCode, Long excludeId) {
        long count = projectMapper.selectCount(Wrappers.<BizProject>lambdaQuery()
                .eq(BizProject::getProjectCode, projectCode)
                .ne(excludeId != null, BizProject::getId, excludeId));
        if (count > 0) {
            throw new BusException("项目编码已存在");
        }
    }

    private ProjectVO toVO(BizProject project, boolean includeMembers) {
        ProjectVO vo = new ProjectVO();
        BeanUtils.copyProperties(project, vo);
        BizEnterprise enterprise = enterpriseMapper.selectById(project.getEnterpriseId());
        if (enterprise != null) {
            vo.setEnterpriseName(enterprise.getEnterpriseName());
        }
        if (includeMembers) {
            // 列表接口不加载成员，避免分页场景产生 N+1 查询；详情接口才返回成员信息。
            vo.setMembers(projectMemberMapper.selectList(Wrappers.<BizProjectMember>lambdaQuery()
                    .eq(BizProjectMember::getProjectId, project.getId())).stream().map(member -> {
                ProjectMemberVO memberVO = new ProjectMemberVO();
                BeanUtils.copyProperties(member, memberVO);
                return memberVO;
            }).toList());
        }
        return vo;
    }
}
