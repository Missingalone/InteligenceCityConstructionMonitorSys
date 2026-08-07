package com.cyx.adminterminal.entity.vo;
import lombok.Data;
/** 管理员一期运营总览指标。 */ @Data public class DashboardStatsVO { private Long projectCount; private Long onlineDeviceCount; private Long pendingAlarmCount; private Long activeRectificationCount; private Long pendingFeedbackCount; }
