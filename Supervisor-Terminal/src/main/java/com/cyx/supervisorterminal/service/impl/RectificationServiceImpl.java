package com.cyx.supervisorterminal.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cyx.exception.BusException;
import com.cyx.supervisorterminal.entity.dto.*;
import com.cyx.supervisorterminal.entity.po.*;
import com.cyx.supervisorterminal.entity.vo.RectificationOrderVO;
import com.cyx.supervisorterminal.mapper.*;
import com.cyx.supervisorterminal.security.CurrentUserAccess;
import com.cyx.supervisorterminal.service.RectificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/** 整改闭环监管侧实现，保证告警、项目和施工企业关系一致。 */
@Service
public class RectificationServiceImpl implements RectificationService {
    private final BizRectificationOrderMapper orderMapper; private final BizAlarmRecordMapper alarmMapper;
    private final BizProjectMapper projectMapper; private final CurrentUserAccess access;

    public RectificationServiceImpl(BizRectificationOrderMapper orderMapper, BizAlarmRecordMapper alarmMapper,
                                     BizProjectMapper projectMapper, CurrentUserAccess access) {
        this.orderMapper = orderMapper; this.alarmMapper = alarmMapper; this.projectMapper = projectMapper; this.access = access;
    }

    /** {@inheritDoc} */
    @Override public List<RectificationOrderVO> list() {
        List<BizRectificationOrder> orders = access.canAccessAllProjects()
                ? orderMapper.selectList(Wrappers.<BizRectificationOrder>lambdaQuery().orderByDesc(BizRectificationOrder::getIssuedAt))
                : orderMapper.selectByProjectMemberUsername(access.username());
        return orders.stream().map(this::toVO).toList();
    }

    /** {@inheritDoc} */
    @Override @Transactional(rollbackFor = Exception.class)
    public Long issue(RectificationIssueDTO dto) {
        BizAlarmRecord alarm = alarmMapper.selectById(dto.getAlarmId());
        if (alarm == null) throw new BusException("告警不存在");
        requireProjectAccess(alarm.getProjectId());
        // 已解决或已关闭告警不允许重新进入整改流程。
        if (!"PENDING".equals(alarm.getAlarmStatus()) && !"HANDLING".equals(alarm.getAlarmStatus())) {
            throw new BusException("当前告警状态不允许下发整改");
        }
        long active = orderMapper.selectCount(Wrappers.<BizRectificationOrder>lambdaQuery()
                // 被驳回的整改单应由企业重新提交，不能再创建一张重复整改单。
                .eq(BizRectificationOrder::getAlarmId, alarm.getId())
                .in(BizRectificationOrder::getStatus, "PENDING", "SUBMITTED", "REJECTED"));
        if (active > 0) throw new BusException("该告警已有未完成整改单");
        BizProject project = projectMapper.selectById(alarm.getProjectId());
        BizRectificationOrder order = new BizRectificationOrder();
        order.setOrderNo("RO" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        order.setProjectId(project.getId()); order.setAlarmId(alarm.getId()); order.setEnterpriseId(project.getEnterpriseId());
        order.setTitle(dto.getTitle()); order.setContent(dto.getContent()); order.setDeadlineAt(dto.getDeadlineAt());
        order.setStatus("PENDING"); order.setIssuedBy(access.userId()); order.setIssuedAt(LocalDateTime.now());
        orderMapper.insert(order);
        alarm.setAlarmStatus("HANDLING"); alarmMapper.updateById(alarm);
        return order.getId();
    }

    /** {@inheritDoc} */
    @Override @Transactional(rollbackFor = Exception.class)
    public void review(Long id, RectificationReviewDTO dto) {
        BizRectificationOrder order = requireOrder(id);
        if (!"SUBMITTED".equals(order.getStatus())) throw new BusException("整改单尚未提交复查");
        order.setStatus(dto.isApproved() ? "APPROVED" : "REJECTED"); order.setReviewedBy(access.userId());
        order.setReviewedAt(LocalDateTime.now()); order.setReviewRemark(dto.getReviewRemark()); orderMapper.updateById(order);
        if (dto.isApproved() && order.getAlarmId() != null) {
            BizAlarmRecord alarm = alarmMapper.selectById(order.getAlarmId());
            if (alarm != null) { alarm.setAlarmStatus("RESOLVED"); alarmMapper.updateById(alarm); }
        }
    }

    /** 查询并校验整改单数据权限。 */
    private BizRectificationOrder requireOrder(Long id) {
        BizRectificationOrder order = orderMapper.selectById(id);
        if (order == null) throw new BusException("整改单不存在");
        requireProjectAccess(order.getProjectId()); return order;
    }

    /** 校验当前账号能否操作项目。 */
    private void requireProjectAccess(Long projectId) {
        if (!access.canAccessAllProjects() && projectMapper.countAccessibleProject(projectId, access.username()) == 0)
            throw new BusException("无权访问该项目整改单");
    }

    /** 转换为接口返回对象。 */
    private RectificationOrderVO toVO(BizRectificationOrder order) {
        RectificationOrderVO vo = new RectificationOrderVO(); BeanUtils.copyProperties(order, vo); return vo;
    }
}
