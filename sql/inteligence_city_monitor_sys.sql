/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80046 (8.0.46)
 Source Host           : localhost:3306
 Source Schema         : inteligence_city_monitor_sys

 Target Server Type    : MySQL
 Target Server Version : 80046 (8.0.46)
 File Encoding         : 65001

 Date: 18/08/2026 09:07:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for biz_alarm_record
-- ----------------------------
DROP TABLE IF EXISTS `biz_alarm_record`;
CREATE TABLE `biz_alarm_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `alarm_no` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `project_id` bigint NOT NULL,
  `device_id` bigint DEFAULT NULL,
  `rule_id` bigint DEFAULT NULL,
  `alarm_type` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `alarm_level` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `alarm_title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `alarm_content` text COLLATE utf8mb4_unicode_ci,
  `threshold_value` decimal(12,2) DEFAULT NULL,
  `actual_value` decimal(12,2) DEFAULT NULL,
  `alarm_status` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING',
  `handler_id` bigint DEFAULT NULL,
  `handled_at` datetime DEFAULT NULL,
  `handle_remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `triggered_at` datetime NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_alarm_record_no` (`alarm_no`),
  KEY `idx_alarm_project_status` (`project_id`,`alarm_status`),
  KEY `idx_alarm_device_id` (`device_id`),
  KEY `idx_alarm_triggered_at` (`triggered_at`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Business alarm record';

-- ----------------------------
-- Records of biz_alarm_record
-- ----------------------------
BEGIN;
INSERT INTO `biz_alarm_record` (`id`, `alarm_no`, `project_id`, `device_id`, `rule_id`, `alarm_type`, `alarm_level`, `alarm_title`, `alarm_content`, `threshold_value`, `actual_value`, `alarm_status`, `handler_id`, `handled_at`, `handle_remark`, `triggered_at`, `created_at`, `updated_at`, `deleted`) VALUES (1, 'ALDEMO20260804', 1, 1, 1, 'DEVICE_THRESHOLD', 'WARNING', '示范项目扬尘监测仪触发PM10超限', 'pm10 实际值 86.20，阈值 80', 80.00, 86.20, 'PENDING', NULL, NULL, NULL, '2026-08-04 19:50:36', '2026-08-04 19:50:36', '2026-08-11 10:15:32', 0);
INSERT INTO `biz_alarm_record` (`id`, `alarm_no`, `project_id`, `device_id`, `rule_id`, `alarm_type`, `alarm_level`, `alarm_title`, `alarm_content`, `threshold_value`, `actual_value`, `alarm_status`, `handler_id`, `handled_at`, `handle_remark`, `triggered_at`, `created_at`, `updated_at`, `deleted`) VALUES (2, 'DEMO_ALARM_001', 3, 5, 2, 'DEVICE_THRESHOLD', 'HIGH', '轨交项目PM2.5超限', 'PM2.5实际值92.60，阈值75.00', 75.00, 92.60, 'PENDING', NULL, NULL, NULL, '2026-08-11 23:16:52', '2026-08-11 23:21:52', '2026-08-11 23:21:52', 0);
INSERT INTO `biz_alarm_record` (`id`, `alarm_no`, `project_id`, `device_id`, `rule_id`, `alarm_type`, `alarm_level`, `alarm_title`, `alarm_content`, `threshold_value`, `actual_value`, `alarm_status`, `handler_id`, `handled_at`, `handle_remark`, `triggered_at`, `created_at`, `updated_at`, `deleted`) VALUES (3, 'DEMO_ALARM_002', 3, 5, 3, 'DEVICE_THRESHOLD', 'MEDIUM', '轨交项目噪声超限', '噪声实际值76.50dB，阈值70.00dB', 70.00, 76.50, 'HANDLING', NULL, NULL, NULL, '2026-08-11 22:51:52', '2026-08-11 23:21:52', '2026-08-11 23:21:52', 0);
INSERT INTO `biz_alarm_record` (`id`, `alarm_no`, `project_id`, `device_id`, `rule_id`, `alarm_type`, `alarm_level`, `alarm_title`, `alarm_content`, `threshold_value`, `actual_value`, `alarm_status`, `handler_id`, `handled_at`, `handle_remark`, `triggered_at`, `created_at`, `updated_at`, `deleted`) VALUES (4, 'DEMO_ALARM_003', 2, 4, NULL, 'DEVICE_OFFLINE', 'LOW', '塔吊通信短暂中断', '设备曾发生短暂离线，现已恢复', NULL, NULL, 'RESOLVED', NULL, NULL, NULL, '2026-08-10 23:21:52', '2026-08-11 23:21:52', '2026-08-11 23:21:52', 0);
COMMIT;

-- ----------------------------
-- Table structure for biz_alarm_rule
-- ----------------------------
DROP TABLE IF EXISTS `biz_alarm_rule`;
CREATE TABLE `biz_alarm_rule` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `rule_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `rule_code` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `device_type` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `metric_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `comparison_operator` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `threshold_value` decimal(12,2) DEFAULT NULL,
  `alarm_level` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `enabled` tinyint NOT NULL DEFAULT '1',
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_alarm_rule_code` (`rule_code`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Alarm rule';

-- ----------------------------
-- Records of biz_alarm_rule
-- ----------------------------
BEGIN;
INSERT INTO `biz_alarm_rule` (`id`, `rule_name`, `rule_code`, `device_type`, `metric_name`, `comparison_operator`, `threshold_value`, `alarm_level`, `enabled`, `remark`, `created_at`, `updated_at`, `deleted`) VALUES (1, 'PM10超限', 'DUST_PM10_HIGH', 'DUST', 'pm10', 'GT', 80.00, 'WARNING', 1, '一期示范规则', '2026-08-04 19:50:36', '2026-08-04 19:50:36', 0);
INSERT INTO `biz_alarm_rule` (`id`, `rule_name`, `rule_code`, `device_type`, `metric_name`, `comparison_operator`, `threshold_value`, `alarm_level`, `enabled`, `remark`, `created_at`, `updated_at`, `deleted`) VALUES (2, 'PM2.5超限预警', 'DEMO_RULE_PM25', 'DUST', 'pm25', 'GT', 75.00, 'HIGH', 1, '演示规则', '2026-08-11 23:21:52', '2026-08-11 23:21:52', 0);
INSERT INTO `biz_alarm_rule` (`id`, `rule_name`, `rule_code`, `device_type`, `metric_name`, `comparison_operator`, `threshold_value`, `alarm_level`, `enabled`, `remark`, `created_at`, `updated_at`, `deleted`) VALUES (3, '噪声超限预警', 'DEMO_RULE_NOISE', 'DUST', 'noiseDb', 'GT', 70.00, 'MEDIUM', 1, '演示规则', '2026-08-11 23:21:52', '2026-08-11 23:21:52', 0);
INSERT INTO `biz_alarm_rule` (`id`, `rule_name`, `rule_code`, `device_type`, `metric_name`, `comparison_operator`, `threshold_value`, `alarm_level`, `enabled`, `remark`, `created_at`, `updated_at`, `deleted`) VALUES (4, '大风塔吊预警', 'DEMO_RULE_WIND', 'TOWER_CRANE', 'windSpeed', 'GTE', 10.80, 'CRITICAL', 1, '演示规则', '2026-08-11 23:21:52', '2026-08-11 23:21:52', 0);
COMMIT;

-- ----------------------------
-- Table structure for biz_device
-- ----------------------------
DROP TABLE IF EXISTS `biz_device`;
CREATE TABLE `biz_device` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `device_code` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `device_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `project_id` bigint NOT NULL,
  `device_type` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `manufacturer` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `model` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `installation_location` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'OFFLINE',
  `last_online_at` datetime DEFAULT NULL,
  `installed_at` datetime DEFAULT NULL,
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_device_code` (`device_code`),
  KEY `idx_device_project_id` (`project_id`),
  KEY `idx_device_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Monitoring device';

-- ----------------------------
-- Records of biz_device
-- ----------------------------
BEGIN;
INSERT INTO `biz_device` (`id`, `device_code`, `device_name`, `project_id`, `device_type`, `manufacturer`, `model`, `installation_location`, `status`, `last_online_at`, `installed_at`, `remark`, `created_at`, `updated_at`, `deleted`) VALUES (1, 'DUST-DEMO-001', '示范项目扬尘监测仪', 1, 'DUST', '示范设备厂商', 'DUST-100', '项目东侧围挡', 'ONLINE', '2026-08-04 20:11:30', '2026-08-04 19:24:12', '用于一期监测数据联调的模拟设备', '2026-08-04 19:24:12', '2026-08-04 19:30:42', 0);
INSERT INTO `biz_device` (`id`, `device_code`, `device_name`, `project_id`, `device_type`, `manufacturer`, `model`, `installation_location`, `status`, `last_online_at`, `installed_at`, `remark`, `created_at`, `updated_at`, `deleted`) VALUES (2, 'DUST-DEMO-002', '塔吊设备', 1, 'DUST', '示范设备厂商', 'DUST-100', '项目西侧围挡', 'ONLINE', '2026-08-10 15:42:25', '2026-08-11 15:42:31', '用于一期建设', '2026-08-11 15:42:45', '2026-08-11 15:42:45', 0);
INSERT INTO `biz_device` (`id`, `device_code`, `device_name`, `project_id`, `device_type`, `manufacturer`, `model`, `installation_location`, `status`, `last_online_at`, `installed_at`, `remark`, `created_at`, `updated_at`, `deleted`) VALUES (3, 'DEMO_ENV_001', '一期扬尘环境监测站', 2, 'DUST', '城市感知科技', 'ENV-Pro X1', '项目东门', 'ONLINE', '2026-08-11 23:21:52', '2026-01-16 09:00:00', '演示环境设备', '2026-08-11 23:21:52', '2026-08-11 23:21:52', 0);
INSERT INTO `biz_device` (`id`, `device_code`, `device_name`, `project_id`, `device_type`, `manufacturer`, `model`, `installation_location`, `status`, `last_online_at`, `installed_at`, `remark`, `created_at`, `updated_at`, `deleted`) VALUES (4, 'DEMO_TOWER_001', '1号塔吊', 2, 'TOWER_CRANE', '中联重科', 'TC6013', '1号楼南侧', 'ONLINE', '2026-08-11 23:21:52', '2026-01-20 10:00:00', '演示塔吊', '2026-08-11 23:21:52', '2026-08-11 23:21:52', 0);
INSERT INTO `biz_device` (`id`, `device_code`, `device_name`, `project_id`, `device_type`, `manufacturer`, `model`, `installation_location`, `status`, `last_online_at`, `installed_at`, `remark`, `created_at`, `updated_at`, `deleted`) VALUES (5, 'DEMO_ENV_002', '轨交项目环境监测站', 3, 'DUST', '城市感知科技', 'ENV-Pro X2', '基坑北侧', 'ALARM', '2026-08-11 23:16:52', '2026-03-09 08:30:00', '演示报警设备', '2026-08-11 23:21:52', '2026-08-11 23:21:52', 0);
INSERT INTO `biz_device` (`id`, `device_code`, `device_name`, `project_id`, `device_type`, `manufacturer`, `model`, `installation_location`, `status`, `last_online_at`, `installed_at`, `remark`, `created_at`, `updated_at`, `deleted`) VALUES (6, 'DEMO_ELEVATOR_001', '1号施工升降机', 3, 'ELEVATOR', '上海建机', 'SC200', '施工区西侧', 'OFFLINE', '2026-08-11 21:21:52', '2026-03-12 14:00:00', '演示离线设备', '2026-08-11 23:21:52', '2026-08-11 23:21:52', 0);
COMMIT;

-- ----------------------------
-- Table structure for biz_device_monitor_data
-- ----------------------------
DROP TABLE IF EXISTS `biz_device_monitor_data`;
CREATE TABLE `biz_device_monitor_data` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `device_id` bigint NOT NULL,
  `project_id` bigint NOT NULL,
  `pm25` decimal(10,2) DEFAULT NULL,
  `pm10` decimal(10,2) DEFAULT NULL,
  `noise_db` decimal(10,2) DEFAULT NULL,
  `temperature` decimal(10,2) DEFAULT NULL,
  `humidity` decimal(10,2) DEFAULT NULL,
  `wind_speed` decimal(10,2) DEFAULT NULL,
  `wind_direction` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `raw_data` json DEFAULT NULL,
  `collected_at` datetime NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_monitor_data_device_time` (`device_id`,`collected_at`),
  KEY `idx_monitor_data_project_time` (`project_id`,`collected_at`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Device monitoring time series';

-- ----------------------------
-- Records of biz_device_monitor_data
-- ----------------------------
BEGIN;
INSERT INTO `biz_device_monitor_data` (`id`, `device_id`, `project_id`, `pm25`, `pm10`, `noise_db`, `temperature`, `humidity`, `wind_speed`, `wind_direction`, `raw_data`, `collected_at`, `created_at`) VALUES (1, 1, 1, 48.50, 86.20, 63.40, 28.10, 56.00, 2.40, '东南风', NULL, '2026-08-04 19:30:42', '2026-08-04 19:30:42');
INSERT INTO `biz_device_monitor_data` (`id`, `device_id`, `project_id`, `pm25`, `pm10`, `noise_db`, `temperature`, `humidity`, `wind_speed`, `wind_direction`, `raw_data`, `collected_at`, `created_at`) VALUES (2, 1, 1, 40.00, 95.00, 60.00, NULL, NULL, NULL, NULL, NULL, '2026-08-04 20:11:30', '2026-08-04 20:11:29');
INSERT INTO `biz_device_monitor_data` (`id`, `device_id`, `project_id`, `pm25`, `pm10`, `noise_db`, `temperature`, `humidity`, `wind_speed`, `wind_direction`, `raw_data`, `collected_at`, `created_at`) VALUES (3, 3, 2, 35.20, 62.80, 58.40, 27.60, 61.20, 2.80, '东南', '{\"seed\": \"codex-demo-env-001\"}', '2026-08-11 23:11:52', '2026-08-11 23:21:52');
INSERT INTO `biz_device_monitor_data` (`id`, `device_id`, `project_id`, `pm25`, `pm10`, `noise_db`, `temperature`, `humidity`, `wind_speed`, `wind_direction`, `raw_data`, `collected_at`, `created_at`) VALUES (4, 5, 3, 92.60, 138.40, 76.50, 30.10, 55.80, 5.60, '西北', '{\"seed\": \"codex-demo-env-002\"}', '2026-08-11 23:16:52', '2026-08-11 23:21:52');
COMMIT;

-- ----------------------------
-- Table structure for biz_enterprise
-- ----------------------------
DROP TABLE IF EXISTS `biz_enterprise`;
CREATE TABLE `biz_enterprise` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `organization_id` bigint DEFAULT NULL,
  `enterprise_name` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `unified_social_credit_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `legal_representative` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `contact_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `contact_mobile` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `qualification_info` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` tinyint NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_enterprise_credit_code` (`unified_social_credit_code`),
  KEY `idx_enterprise_organization_id` (`organization_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Construction enterprise';

-- ----------------------------
-- Records of biz_enterprise
-- ----------------------------
BEGIN;
INSERT INTO `biz_enterprise` (`id`, `organization_id`, `enterprise_name`, `unified_social_credit_code`, `legal_representative`, `contact_name`, `contact_mobile`, `address`, `qualification_info`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (1, 2, '示范建设集团有限公司', '91500000DEMO00001X', '李建国', '王建', '13900000001', '智慧城市建设示范区', '建筑工程施工总承包一级', 1, '2026-08-04 17:40:14', '2026-08-04 19:50:36', 0);
COMMIT;

-- ----------------------------
-- Table structure for biz_project
-- ----------------------------
DROP TABLE IF EXISTS `biz_project`;
CREATE TABLE `biz_project` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `project_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `project_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `enterprise_id` bigint NOT NULL,
  `supervisor_org_id` bigint NOT NULL DEFAULT '0',
  `project_type` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `project_status` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PREPARING',
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `longitude` decimal(10,7) DEFAULT NULL,
  `latitude` decimal(10,7) DEFAULT NULL,
  `planned_start_date` date DEFAULT NULL,
  `planned_end_date` date DEFAULT NULL,
  `actual_start_date` date DEFAULT NULL,
  `progress_percent` decimal(5,2) NOT NULL DEFAULT '0.00',
  `project_manager` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `manager_mobile` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  `actual_end_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_project_code` (`project_code`),
  KEY `idx_project_enterprise_id` (`enterprise_id`),
  KEY `idx_project_status` (`project_status`),
  KEY `idx_project_supervisor_org_id` (`supervisor_org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Construction project';

-- ----------------------------
-- Records of biz_project
-- ----------------------------
BEGIN;
INSERT INTO `biz_project` (`id`, `project_code`, `project_name`, `enterprise_id`, `supervisor_org_id`, `project_type`, `project_status`, `address`, `longitude`, `latitude`, `planned_start_date`, `planned_end_date`, `actual_start_date`, `progress_percent`, `project_manager`, `manager_mobile`, `description`, `created_at`, `updated_at`, `deleted`, `actual_end_date`) VALUES (1, 'DEMO-2026-001', '智慧城市建设示范项目一期', 1, 1, 'BUILDING', 'CONSTRUCTING', '智慧城市建设示范区 A 区', 127.0000000, 29.1200000, '2026-01-01', '2026-12-31', '2026-08-14', 15.00, '王建', '13900000001', '用于监管端项目管理联调的初始化项目', '2026-08-04 17:40:14', '2026-08-11 22:54:27', 0, NULL);
INSERT INTO `biz_project` (`id`, `project_code`, `project_name`, `enterprise_id`, `supervisor_org_id`, `project_type`, `project_status`, `address`, `longitude`, `latitude`, `planned_start_date`, `planned_end_date`, `actual_start_date`, `progress_percent`, `project_manager`, `manager_mobile`, `description`, `created_at`, `updated_at`, `deleted`, `actual_end_date`) VALUES (2, 'DEMO_PRJ_001', '智慧新城一期示范项目', 1, 1, 'HOUSING', 'CONSTRUCTING', '上海市浦东新区示范路88号', 121.5441200, 31.2213100, '2026-01-10', '2027-06-30', '2026-01-15', 42.50, '张工', '13800001001', '智慧城市监测系统演示项目', '2026-08-11 23:21:52', '2026-08-11 23:21:52', 0, NULL);
INSERT INTO `biz_project` (`id`, `project_code`, `project_name`, `enterprise_id`, `supervisor_org_id`, `project_type`, `project_status`, `address`, `longitude`, `latitude`, `planned_start_date`, `planned_end_date`, `actual_start_date`, `progress_percent`, `project_manager`, `manager_mobile`, `description`, `created_at`, `updated_at`, `deleted`, `actual_end_date`) VALUES (3, 'DEMO_PRJ_002', '城市轨道交通安全示范工程', 1, 1, 'MUNICIPAL', 'CONSTRUCTING', '上海市徐汇区建设路66号', 121.4365200, 31.1884300, '2026-03-01', '2028-02-28', '2026-03-08', 18.00, '李工', '13800001002', '基坑与环境监测演示项目', '2026-08-11 23:21:52', '2026-08-11 23:21:52', 0, NULL);
COMMIT;

-- ----------------------------
-- Table structure for biz_project_member
-- ----------------------------
DROP TABLE IF EXISTS `biz_project_member`;
CREATE TABLE `biz_project_member` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `project_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `member_role` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_project_member` (`project_id`,`user_id`),
  KEY `idx_project_member_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Project member';

-- ----------------------------
-- Records of biz_project_member
-- ----------------------------
BEGIN;
INSERT INTO `biz_project_member` (`id`, `project_id`, `user_id`, `member_role`, `created_at`) VALUES (1, 1, 2, 'SUPERVISOR', '2026-08-04 17:40:14');
INSERT INTO `biz_project_member` (`id`, `project_id`, `user_id`, `member_role`, `created_at`) VALUES (2, 2, 2, 'SUPERVISOR', '2026-08-11 23:21:52');
INSERT INTO `biz_project_member` (`id`, `project_id`, `user_id`, `member_role`, `created_at`) VALUES (3, 2, 3, 'ENTERPRISE', '2026-08-11 23:21:52');
INSERT INTO `biz_project_member` (`id`, `project_id`, `user_id`, `member_role`, `created_at`) VALUES (4, 3, 2, 'SUPERVISOR', '2026-08-11 23:21:52');
INSERT INTO `biz_project_member` (`id`, `project_id`, `user_id`, `member_role`, `created_at`) VALUES (5, 3, 3, 'ENTERPRISE', '2026-08-11 23:21:52');
COMMIT;

-- ----------------------------
-- Table structure for biz_public_feedback
-- ----------------------------
DROP TABLE IF EXISTS `biz_public_feedback`;
CREATE TABLE `biz_public_feedback` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `feedback_no` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `project_id` bigint DEFAULT NULL,
  `feedback_type` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `contact_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `contact_mobile` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `attachment_urls` json DEFAULT NULL,
  `status` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING',
  `handler_id` bigint DEFAULT NULL,
  `handled_at` datetime DEFAULT NULL,
  `handle_result` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_public_feedback_no` (`feedback_no`),
  KEY `idx_public_feedback_project_id` (`project_id`),
  KEY `idx_public_feedback_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Public feedback';

-- ----------------------------
-- Records of biz_public_feedback
-- ----------------------------
BEGIN;
INSERT INTO `biz_public_feedback` (`id`, `feedback_no`, `project_id`, `feedback_type`, `content`, `contact_name`, `contact_mobile`, `attachment_urls`, `status`, `handler_id`, `handled_at`, `handle_result`, `created_at`, `updated_at`, `deleted`) VALUES (1, 'DEMO_FEEDBACK_001', 2, 'NOISE', '夜间施工噪声较大，请核查施工时段。', '周先生', '13900003001', '[\"/demo/feedback/noise-001.jpg\"]', 'PENDING', NULL, NULL, NULL, '2026-08-11 23:21:52', '2026-08-11 23:21:52', 0);
INSERT INTO `biz_public_feedback` (`id`, `feedback_no`, `project_id`, `feedback_type`, `content`, `contact_name`, `contact_mobile`, `attachment_urls`, `status`, `handler_id`, `handled_at`, `handle_result`, `created_at`, `updated_at`, `deleted`) VALUES (2, 'DEMO_FEEDBACK_002', 3, 'DUST', '道路扬尘明显，建议增加洒水频次。', '陈女士', '13900003002', '[]', 'PROCESSING', NULL, NULL, NULL, '2026-08-11 23:21:52', '2026-08-11 23:21:52', 0);
COMMIT;

-- ----------------------------
-- Table structure for biz_rectification_order
-- ----------------------------
DROP TABLE IF EXISTS `biz_rectification_order`;
CREATE TABLE `biz_rectification_order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `project_id` bigint NOT NULL,
  `alarm_id` bigint DEFAULT NULL,
  `enterprise_id` bigint NOT NULL,
  `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `deadline_at` datetime NOT NULL,
  `status` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING',
  `issued_by` bigint NOT NULL,
  `issued_at` datetime NOT NULL,
  `submitted_by` bigint DEFAULT NULL,
  `submitted_at` datetime DEFAULT NULL,
  `result_description` text COLLATE utf8mb4_unicode_ci,
  `evidence_urls` json DEFAULT NULL,
  `reviewed_by` bigint DEFAULT NULL,
  `reviewed_at` datetime DEFAULT NULL,
  `review_remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_rectification_order_no` (`order_no`),
  KEY `idx_rectification_project_status` (`project_id`,`status`),
  KEY `idx_rectification_enterprise_id` (`enterprise_id`),
  KEY `idx_rectification_deadline_at` (`deadline_at`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Rectification workflow';

-- ----------------------------
-- Records of biz_rectification_order
-- ----------------------------
BEGIN;
INSERT INTO `biz_rectification_order` (`id`, `order_no`, `project_id`, `alarm_id`, `enterprise_id`, `title`, `content`, `deadline_at`, `status`, `issued_by`, `issued_at`, `submitted_by`, `submitted_at`, `result_description`, `evidence_urls`, `reviewed_by`, `reviewed_at`, `review_remark`, `created_at`, `updated_at`, `deleted`) VALUES (1, 'DEMO_RECT_001', 3, 2, 1, '轨交项目扬尘超限整改', '立即开启喷淋并检查裸土覆盖，整改完成后上传现场佐证。', '2026-08-13 23:21:52', 'PENDING', 2, '2026-08-11 23:21:52', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-11 23:21:52', '2026-08-11 23:21:52', 0);
COMMIT;

-- ----------------------------
-- Table structure for city_camera
-- ----------------------------
DROP TABLE IF EXISTS `city_camera`;
CREATE TABLE `city_camera` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '摄像头ID',
  `camera_code` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '摄像头编号',
  `camera_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '摄像头名称',
  `project_id` bigint DEFAULT NULL COMMENT '所属施工项目ID',
  `foundation_pit_id` bigint DEFAULT NULL COMMENT '所属基坑ID',
  `camera_type` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '摄像头类型：普通摄像头、球机、枪机、全景摄像头',
  `device_model` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '设备型号',
  `manufacturer` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '设备厂商',
  `installation_address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '安装地址',
  `direction` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '监控方向',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0离线，1在线，2故障，3停用',
  `has_audio` tinyint NOT NULL DEFAULT '0' COMMENT '是否支持音频：0否，1是',
  `has_ptz` tinyint NOT NULL DEFAULT '0' COMMENT '是否支持云台控制：0否，1是',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `created_by` bigint DEFAULT NULL COMMENT '创建人',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` bigint DEFAULT NULL COMMENT '修改人',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0否，1是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `camera_code` (`camera_code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='智慧城市施工监控摄像头表';

-- ----------------------------
-- Records of city_camera
-- ----------------------------
BEGIN;
INSERT INTO `city_camera` (`id`, `camera_code`, `camera_name`, `project_id`, `foundation_pit_id`, `camera_type`, `device_model`, `manufacturer`, `installation_address`, `direction`, `status`, `has_audio`, `has_ptz`, `remark`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`) VALUES (1, 'DEMO_CAM_001', '轨交基坑全景摄像头', 3, 1, '全景摄像头', 'IPC-Pano-8K', '海康威视', '基坑北侧塔架', '面向基坑中心', 1, 1, 1, '演示在线摄像头', 2, '2026-08-11 23:21:52', 2, '2026-08-11 23:21:52', 0);
INSERT INTO `city_camera` (`id`, `camera_code`, `camera_name`, `project_id`, `foundation_pit_id`, `camera_type`, `device_model`, `manufacturer`, `installation_address`, `direction`, `status`, `has_audio`, `has_ptz`, `remark`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`) VALUES (2, 'DEMO_CAM_002', '地下室基坑枪机', 2, 2, '枪机', 'IPC-Bullet-4M', '大华股份', '2号楼东南角', '面向出入口', 1, 0, 0, '演示固定摄像头', 2, '2026-08-11 23:21:52', 2, '2026-08-11 23:21:52', 0);
INSERT INTO `city_camera` (`id`, `camera_code`, `camera_name`, `project_id`, `foundation_pit_id`, `camera_type`, `device_model`, `manufacturer`, `installation_address`, `direction`, `status`, `has_audio`, `has_ptz`, `remark`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`) VALUES (3, 'DEMO_CAM_003', '塔吊吊钩摄像头', 2, NULL, '球机', 'IPC-PTZ-4M', '宇视科技', '1号塔吊驾驶室下方', '跟随吊钩', 0, 0, 1, '演示离线摄像头', 2, '2026-08-11 23:21:52', 2, '2026-08-11 23:21:52', 0);
COMMIT;

-- ----------------------------
-- Table structure for construction_foundation_pit
-- ----------------------------
DROP TABLE IF EXISTS `construction_foundation_pit`;
CREATE TABLE `construction_foundation_pit` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '基坑ID',
  `pit_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '基坑编号',
  `pit_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '基坑名称',
  `project_id` bigint NOT NULL COMMENT '所属施工项目ID',
  `pit_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '基坑类型：地下室、地铁、管廊、桥梁承台等',
  `excavation_method` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '开挖方式：放坡开挖、支护开挖、分层开挖',
  `area` decimal(12,2) DEFAULT NULL COMMENT '基坑面积，单位：平方米',
  `support_type` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '支护形式：排桩、地下连续墙、土钉墙、锚杆等',
  `support_scheme` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '支护方案',
  `excavation_start_date` date DEFAULT NULL COMMENT '基坑开挖开始日期',
  `excavation_end_date` date DEFAULT NULL COMMENT '基坑开挖结束日期',
  `backfill_date` date DEFAULT NULL COMMENT '基坑回填日期',
  `current_stage` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '当前阶段：未开挖、土方开挖、支护施工、降水施工、基础施工、已回填',
  `progress` decimal(5,2) DEFAULT '0.00' COMMENT '基坑施工进度百分比',
  `risk_level` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '风险等级：低、中、高、重大',
  `monitoring_status` tinyint NOT NULL DEFAULT '0' COMMENT '监测状态：0正常，1预警，2报警，3停止施工',
  `warning_threshold` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '预警阈值配置',
  `alarm_reason` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '报警原因',
  `longitude` decimal(10,7) DEFAULT NULL COMMENT '基坑经度',
  `latitude` decimal(10,7) DEFAULT NULL COMMENT '基坑纬度',
  `location_address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '基坑位置',
  `responsible_person` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '现场负责人',
  `responsible_phone` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '负责人电话',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '基坑描述',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `created_by` bigint DEFAULT NULL COMMENT '创建人',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` bigint DEFAULT NULL COMMENT '修改人',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0否，1是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `pit_code` (`pit_code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='智慧城市施工基坑表';

-- ----------------------------
-- Records of construction_foundation_pit
-- ----------------------------
BEGIN;
INSERT INTO `construction_foundation_pit` (`id`, `pit_code`, `pit_name`, `project_id`, `pit_type`, `excavation_method`, `area`, `support_type`, `support_scheme`, `excavation_start_date`, `excavation_end_date`, `backfill_date`, `current_stage`, `progress`, `risk_level`, `monitoring_status`, `warning_threshold`, `alarm_reason`, `longitude`, `latitude`, `location_address`, `responsible_person`, `responsible_phone`, `description`, `remark`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`) VALUES (1, 'DEMO_PIT_001', '轨交车站主体基坑', 3, '地铁', '分层开挖', 8650.00, '地下连续墙', '地下连续墙+三道钢支撑', '2026-04-01', '2027-01-31', NULL, '支护施工', 28.50, '高', 1, '围护墙水平位移累计30mm，日变化3mm', NULL, 121.4367300, 31.1882500, '轨交项目施工区中央', '王工', '13800002001', '深基坑安全监测示范点', '演示基坑', 2, '2026-08-11 23:21:52', 2, '2026-08-11 23:21:52', 0);
INSERT INTO `construction_foundation_pit` (`id`, `pit_code`, `pit_name`, `project_id`, `pit_type`, `excavation_method`, `area`, `support_type`, `support_scheme`, `excavation_start_date`, `excavation_end_date`, `backfill_date`, `current_stage`, `progress`, `risk_level`, `monitoring_status`, `warning_threshold`, `alarm_reason`, `longitude`, `latitude`, `location_address`, `responsible_person`, `responsible_phone`, `description`, `remark`, `created_by`, `created_time`, `updated_by`, `updated_time`, `deleted`) VALUES (2, 'DEMO_PIT_002', '一期地下室基坑', 2, '地下室', '支护开挖', 3200.00, '土钉墙', '放坡结合土钉墙支护', '2026-02-01', '2026-08-31', NULL, '基础施工', 68.00, '中', 0, '坡顶位移累计25mm', NULL, 121.5443600, 31.2211500, '一期项目2号楼区域', '赵工', '13800002002', '地下室基坑监测点', '演示基坑', 2, '2026-08-11 23:21:52', 2, '2026-08-11 23:21:52', 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dict_type_id` bigint NOT NULL,
  `item_label` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `item_value` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sort_order` int NOT NULL DEFAULT '0',
  `status` tinyint NOT NULL DEFAULT '1',
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dict_item_value` (`dict_type_id`,`item_value`),
  KEY `idx_dict_item_type_id` (`dict_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Dictionary item';

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict_item` (`id`, `dict_type_id`, `item_label`, `item_value`, `sort_order`, `status`, `remark`, `created_at`, `updated_at`, `deleted`) VALUES (1, 1, '在线', 'ONLINE', 1, 1, '演示字典项', '2026-08-11 23:21:52', '2026-08-11 23:21:52', 0);
INSERT INTO `sys_dict_item` (`id`, `dict_type_id`, `item_label`, `item_value`, `sort_order`, `status`, `remark`, `created_at`, `updated_at`, `deleted`) VALUES (2, 1, '离线', 'OFFLINE', 2, 1, '演示字典项', '2026-08-11 23:21:52', '2026-08-11 23:21:52', 0);
INSERT INTO `sys_dict_item` (`id`, `dict_type_id`, `item_label`, `item_value`, `sort_order`, `status`, `remark`, `created_at`, `updated_at`, `deleted`) VALUES (3, 1, '报警', 'ALARM', 3, 1, '演示字典项', '2026-08-11 23:21:52', '2026-08-11 23:21:52', 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dict_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `dict_code` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` tinyint NOT NULL DEFAULT '1',
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dict_type_code` (`dict_code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Dictionary type';

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict_type` (`id`, `dict_name`, `dict_code`, `status`, `remark`, `created_at`, `updated_at`, `deleted`) VALUES (1, '设备运行状态', 'DEMO_DEVICE_STATUS', 1, '演示字典', '2026-08-11 23:21:52', '2026-08-11 23:21:52', 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint NOT NULL DEFAULT '0',
  `menu_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `menu_type` char(1) COLLATE utf8mb4_unicode_ci NOT NULL,
  `route_path` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `component` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `permission_code` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `icon` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sort_order` int NOT NULL DEFAULT '0',
  `visible` tinyint NOT NULL DEFAULT '1',
  `status` tinyint NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_menu_parent_id` (`parent_id`),
  KEY `idx_menu_permission_code` (`permission_code`)
) ENGINE=InnoDB AUTO_INCREMENT=504 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Menu and permission';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (1, 0, '系统管理', 'M', '/system', NULL, NULL, 'Setting', 1, 1, 1, '2026-08-04 15:58:19', '2026-08-04 15:58:19', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (2, 1, '用户管理', 'C', '/system/user', NULL, NULL, 'User', 1, 1, 1, '2026-08-04 15:58:19', '2026-08-04 15:58:19', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (3, 1, '角色管理', 'C', '/system/role', NULL, NULL, 'UserRoundCog', 2, 1, 1, '2026-08-04 15:58:19', '2026-08-04 15:58:19', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (4, 1, '组织管理', 'C', '/system/organization', NULL, NULL, 'Network', 3, 1, 1, '2026-08-04 15:58:19', '2026-08-04 15:58:19', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (5, 1, '菜单管理', 'C', '/system/menu', NULL, NULL, 'Menu', 4, 1, 1, '2026-08-04 15:58:19', '2026-08-04 15:58:19', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (9, 2, '查询用户', 'F', NULL, NULL, 'system:user:list', NULL, 1, 1, 1, '2026-08-04 15:58:19', '2026-08-04 15:58:19', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (10, 2, '新增用户', 'F', NULL, NULL, 'system:user:add', NULL, 2, 1, 1, '2026-08-04 15:58:19', '2026-08-04 15:58:19', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (11, 2, '修改用户', 'F', NULL, NULL, 'system:user:update', NULL, 3, 1, 1, '2026-08-04 15:58:19', '2026-08-04 15:58:19', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (12, 2, '删除用户', 'F', NULL, NULL, 'system:user:delete', NULL, 4, 1, 1, '2026-08-04 15:58:19', '2026-08-04 15:58:19', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (13, 2, '重置密码', 'F', NULL, NULL, 'system:user:reset-password', NULL, 5, 1, 1, '2026-08-04 15:58:19', '2026-08-04 15:58:19', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (14, 3, '查询角色', 'F', NULL, NULL, 'system:role:list', NULL, 1, 1, 1, '2026-08-04 15:58:19', '2026-08-04 15:58:19', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (15, 3, '新增角色', 'F', NULL, NULL, 'system:role:add', NULL, 2, 1, 1, '2026-08-04 15:58:19', '2026-08-04 15:58:19', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (16, 3, '修改角色', 'F', NULL, NULL, 'system:role:update', NULL, 3, 1, 1, '2026-08-04 15:58:19', '2026-08-04 15:58:19', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (17, 3, '删除角色', 'F', NULL, NULL, 'system:role:delete', NULL, 4, 1, 1, '2026-08-04 15:58:19', '2026-08-04 15:58:19', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (18, 4, '查询组织', 'F', NULL, NULL, 'system:organization:list', NULL, 1, 1, 1, '2026-08-04 15:58:19', '2026-08-04 15:58:19', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (19, 4, '新增组织', 'F', NULL, NULL, 'system:organization:add', NULL, 2, 1, 1, '2026-08-04 15:58:19', '2026-08-04 15:58:19', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (20, 4, '修改组织', 'F', NULL, NULL, 'system:organization:update', NULL, 3, 1, 1, '2026-08-04 15:58:19', '2026-08-04 15:58:19', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (21, 4, '删除组织', 'F', NULL, NULL, 'system:organization:delete', NULL, 4, 1, 1, '2026-08-04 15:58:19', '2026-08-04 15:58:19', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (22, 5, '查询菜单', 'F', NULL, NULL, 'system:menu:list', NULL, 1, 1, 1, '2026-08-04 15:58:19', '2026-08-04 15:58:19', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (23, 5, '新增菜单', 'F', NULL, NULL, 'system:menu:add', NULL, 2, 1, 1, '2026-08-04 15:58:19', '2026-08-04 15:58:19', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (24, 5, '修改菜单', 'F', NULL, NULL, 'system:menu:update', NULL, 3, 1, 1, '2026-08-04 15:58:19', '2026-08-04 15:58:19', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (25, 5, '删除菜单', 'F', NULL, NULL, 'system:menu:delete', NULL, 4, 1, 1, '2026-08-04 15:58:19', '2026-08-04 15:58:19', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (40, 0, '监管管理', 'M', '/supervisor', NULL, NULL, 'Monitor', 2, 1, 1, '2026-08-04 17:40:14', '2026-08-04 17:40:14', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (41, 40, '企业管理', 'C', '/supervisor/enterprise', NULL, NULL, 'Building2', 1, 1, 1, '2026-08-04 17:40:14', '2026-08-04 17:40:14', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (42, 40, '项目管理', 'C', '/supervisor/project', NULL, NULL, 'FolderKanban', 2, 1, 1, '2026-08-04 17:40:14', '2026-08-04 17:40:14', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (44, 41, '查询企业', 'F', NULL, NULL, 'supervisor:enterprise:list', NULL, 1, 1, 1, '2026-08-04 17:40:14', '2026-08-04 17:40:14', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (45, 41, '查看企业', 'F', NULL, NULL, 'supervisor:enterprise:query', NULL, 2, 1, 1, '2026-08-04 17:40:14', '2026-08-04 17:40:14', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (46, 41, '新增企业', 'F', NULL, NULL, 'supervisor:enterprise:add', NULL, 3, 1, 1, '2026-08-04 17:40:14', '2026-08-04 17:40:14', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (47, 41, '修改企业', 'F', NULL, NULL, 'supervisor:enterprise:edit', NULL, 4, 1, 1, '2026-08-04 17:40:14', '2026-08-04 17:40:14', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (48, 41, '删除企业', 'F', NULL, NULL, 'supervisor:enterprise:delete', NULL, 5, 1, 1, '2026-08-04 17:40:14', '2026-08-04 17:40:14', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (49, 42, '查询项目', 'F', NULL, NULL, 'supervisor:project:list', NULL, 1, 1, 1, '2026-08-04 17:40:14', '2026-08-04 17:40:14', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (50, 42, '查看项目', 'F', NULL, NULL, 'supervisor:project:query', NULL, 2, 1, 1, '2026-08-04 17:40:14', '2026-08-04 17:40:14', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (51, 42, '新增项目', 'F', NULL, NULL, 'supervisor:project:add', NULL, 3, 1, 1, '2026-08-04 17:40:14', '2026-08-04 17:40:14', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (52, 42, '修改项目', 'F', NULL, NULL, 'supervisor:project:edit', NULL, 4, 1, 1, '2026-08-04 17:40:14', '2026-08-04 17:40:14', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (53, 42, '删除项目', 'F', NULL, NULL, 'supervisor:project:delete', NULL, 5, 1, 1, '2026-08-04 17:40:14', '2026-08-04 17:40:14', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (54, 42, '分配项目成员', 'F', NULL, NULL, 'supervisor:project:assign-member', NULL, 6, 1, 1, '2026-08-04 17:40:14', '2026-08-04 17:40:14', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (59, 40, '设备管理', 'C', '/supervisor/device', NULL, NULL, 'Cpu', 3, 1, 1, '2026-08-04 19:24:12', '2026-08-04 19:24:12', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (60, 59, '查询设备', 'F', NULL, NULL, 'supervisor:device:list', NULL, 1, 1, 1, '2026-08-04 19:24:12', '2026-08-04 19:24:12', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (61, 59, '查看设备', 'F', NULL, NULL, 'supervisor:device:query', NULL, 2, 1, 1, '2026-08-04 19:24:12', '2026-08-04 19:24:12', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (62, 59, '新增设备', 'F', NULL, NULL, 'supervisor:device:add', NULL, 3, 1, 1, '2026-08-04 19:24:12', '2026-08-04 19:24:12', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (63, 59, '修改设备', 'F', NULL, NULL, 'supervisor:device:edit', NULL, 4, 1, 1, '2026-08-04 19:24:12', '2026-08-04 19:24:12', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (64, 59, '删除设备', 'F', NULL, NULL, 'supervisor:device:delete', NULL, 5, 1, 1, '2026-08-04 19:24:12', '2026-08-04 19:24:12', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (67, 59, '上报监测数据', 'F', NULL, NULL, 'supervisor:monitor:report', NULL, 6, 1, 1, '2026-08-04 19:30:42', '2026-08-04 19:30:42', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (68, 59, '查询监测数据', 'F', NULL, NULL, 'supervisor:monitor:list', NULL, 7, 1, 1, '2026-08-04 19:30:42', '2026-08-04 19:30:42', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (70, 40, '告警整改', 'C', '/supervisor/alarm', NULL, NULL, 'BellRing', 4, 1, 1, '2026-08-04 19:50:36', '2026-08-04 19:50:36', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (71, 70, '查询告警规则', 'F', NULL, NULL, 'supervisor:alarm-rule:list', NULL, 1, 1, 1, '2026-08-04 19:50:36', '2026-08-04 19:50:36', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (72, 70, '新增告警规则', 'F', NULL, NULL, 'supervisor:alarm-rule:add', NULL, 2, 1, 1, '2026-08-04 19:50:36', '2026-08-04 19:50:36', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (73, 70, '修改告警规则', 'F', NULL, NULL, 'supervisor:alarm-rule:edit', NULL, 3, 1, 1, '2026-08-04 19:50:36', '2026-08-04 19:50:36', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (74, 70, '删除告警规则', 'F', NULL, NULL, 'supervisor:alarm-rule:delete', NULL, 4, 1, 1, '2026-08-04 19:50:36', '2026-08-04 19:50:36', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (75, 70, '查询告警', 'F', NULL, NULL, 'supervisor:alarm:list', NULL, 5, 1, 1, '2026-08-04 19:50:36', '2026-08-04 19:50:36', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (76, 70, '处理告警', 'F', NULL, NULL, 'supervisor:alarm:handle', NULL, 6, 1, 1, '2026-08-04 19:50:36', '2026-08-04 19:50:36', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (77, 70, '查询整改', 'F', NULL, NULL, 'supervisor:rectification:list', NULL, 7, 1, 1, '2026-08-04 19:50:36', '2026-08-04 19:50:36', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (78, 70, '下发整改', 'F', NULL, NULL, 'supervisor:rectification:issue', NULL, 8, 1, 1, '2026-08-04 19:50:36', '2026-08-04 19:50:36', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (79, 70, '复查整改', 'F', NULL, NULL, 'supervisor:rectification:review', NULL, 9, 1, 1, '2026-08-04 19:50:36', '2026-08-04 19:50:36', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (86, 0, '施工企业端', 'M', '/enterprise', NULL, NULL, 'Building2', 3, 1, 1, '2026-08-04 19:50:36', '2026-08-04 19:50:36', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (87, 86, '查询企业项目', 'F', NULL, NULL, 'enterprise:project:list', NULL, 1, 1, 1, '2026-08-04 19:50:36', '2026-08-04 19:50:36', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (88, 86, '上报项目进度', 'F', NULL, NULL, 'enterprise:project:progress', NULL, 2, 1, 1, '2026-08-04 19:50:36', '2026-08-04 19:50:36', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (89, 86, '查询企业告警', 'F', NULL, NULL, 'enterprise:alarm:list', NULL, 3, 1, 1, '2026-08-04 19:50:36', '2026-08-04 19:50:36', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (90, 86, '查询企业整改', 'F', NULL, NULL, 'enterprise:rectification:list', NULL, 4, 1, 1, '2026-08-04 19:50:36', '2026-08-04 19:50:36', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (91, 86, '提交整改结果', 'F', NULL, NULL, 'enterprise:rectification:submit', NULL, 5, 1, 1, '2026-08-04 19:50:36', '2026-08-04 19:50:36', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (94, 1, '查看运营总览', 'F', NULL, NULL, 'admin:dashboard:view', NULL, 1, 1, 1, '2026-08-04 19:50:36', '2026-08-04 19:50:36', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (95, 1, '查询公众反馈', 'F', NULL, NULL, 'admin:feedback:list', NULL, 2, 1, 1, '2026-08-04 19:50:36', '2026-08-04 19:50:36', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (96, 1, '处理公众反馈', 'F', NULL, NULL, 'admin:feedback:handle', NULL, 3, 1, 1, '2026-08-04 19:50:36', '2026-08-04 19:50:36', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (101, 0, '系统管理', 'M', NULL, NULL, NULL, NULL, 1, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (102, 0, '监管业务', 'M', NULL, NULL, NULL, NULL, 2, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (103, 0, '企业工作台', 'M', NULL, NULL, NULL, NULL, 3, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (104, 0, '管理员运营', 'M', NULL, NULL, NULL, NULL, 4, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (201, 101, '用户管理', 'B', NULL, NULL, 'system:user:list', NULL, 1, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (202, 101, '用户查询', 'B', NULL, NULL, 'system:user:query', NULL, 2, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (203, 101, '用户新增', 'B', NULL, NULL, 'system:user:add', NULL, 3, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (204, 101, '用户编辑', 'B', NULL, NULL, 'system:user:edit', NULL, 4, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (205, 101, '用户删除', 'B', NULL, NULL, 'system:user:delete', NULL, 5, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (206, 101, '角色列表', 'B', NULL, NULL, 'system:role:list', NULL, 6, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (207, 101, '角色查询', 'B', NULL, NULL, 'system:role:query', NULL, 7, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (208, 101, '角色编辑', 'B', NULL, NULL, 'system:role:edit', NULL, 8, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (209, 101, '角色删除', 'B', NULL, NULL, 'system:role:delete', NULL, 9, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (210, 101, '角色授权', 'B', NULL, NULL, 'system:role:authorize', NULL, 10, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (211, 101, '菜单管理', 'B', NULL, NULL, 'system:menu:list', NULL, 11, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (212, 101, '菜单编辑', 'B', NULL, NULL, 'system:menu:edit', NULL, 12, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (213, 101, '菜单删除', 'B', NULL, NULL, 'system:menu:delete', NULL, 13, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (214, 101, '组织管理', 'B', NULL, NULL, 'system:organization:list', NULL, 14, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (215, 101, '组织编辑', 'B', NULL, NULL, 'system:organization:edit', NULL, 15, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (216, 101, '组织删除', 'B', NULL, NULL, 'system:organization:delete', NULL, 16, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (217, 101, '字典管理', 'B', NULL, NULL, 'system:dict:list', NULL, 17, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (218, 101, '字典查询', 'B', NULL, NULL, 'system:dict:query', NULL, 18, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (219, 101, '字典编辑', 'B', NULL, NULL, 'system:dict:edit', NULL, 19, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (220, 101, '字典删除', 'B', NULL, NULL, 'system:dict:delete', NULL, 20, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (221, 101, '操作日志', 'B', NULL, NULL, 'system:log:list', NULL, 21, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (301, 102, '项目查询', 'B', NULL, NULL, 'supervisor:project:list', NULL, 1, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (302, 102, '项目操作', 'B', NULL, NULL, 'supervisor:project:add', NULL, 2, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (303, 102, '项目编辑', 'B', NULL, NULL, 'supervisor:project:edit', NULL, 3, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (304, 102, '项目删除', 'B', NULL, NULL, 'supervisor:project:delete', NULL, 4, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (305, 102, '成员分配', 'B', NULL, NULL, 'supervisor:project:assign-member', NULL, 5, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (306, 102, '企业查询', 'B', NULL, NULL, 'supervisor:enterprise:list', NULL, 6, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (307, 102, '企业操作', 'B', NULL, NULL, 'supervisor:enterprise:add', NULL, 7, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (308, 102, '企业编辑', 'B', NULL, NULL, 'supervisor:enterprise:edit', NULL, 8, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (309, 102, '企业删除', 'B', NULL, NULL, 'supervisor:enterprise:delete', NULL, 9, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (310, 102, '设备查询', 'B', NULL, NULL, 'supervisor:device:list', NULL, 10, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (311, 102, '设备操作', 'B', NULL, NULL, 'supervisor:device:add', NULL, 11, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (312, 102, '设备编辑', 'B', NULL, NULL, 'supervisor:device:edit', NULL, 12, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (313, 102, '设备删除', 'B', NULL, NULL, 'supervisor:device:delete', NULL, 13, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (314, 102, '监测数据查询', 'B', NULL, NULL, 'supervisor:monitor:list', NULL, 14, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (315, 102, '监测数据上报', 'B', NULL, NULL, 'supervisor:monitor:report', NULL, 15, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (316, 102, '告警查询', 'B', NULL, NULL, 'supervisor:alarm:list', NULL, 16, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (317, 102, '告警处理', 'B', NULL, NULL, 'supervisor:alarm:handle', NULL, 17, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (318, 102, '告警规则查询', 'B', NULL, NULL, 'supervisor:alarm-rule:list', NULL, 18, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (319, 102, '告警规则新增', 'B', NULL, NULL, 'supervisor:alarm-rule:add', NULL, 19, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (320, 102, '告警规则编辑', 'B', NULL, NULL, 'supervisor:alarm-rule:edit', NULL, 20, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (321, 102, '告警规则删除', 'B', NULL, NULL, 'supervisor:alarm-rule:delete', NULL, 21, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (322, 102, '整改查询', 'B', NULL, NULL, 'supervisor:rectification:list', NULL, 22, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (323, 102, '整改下发', 'B', NULL, NULL, 'supervisor:rectification:issue', NULL, 23, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (324, 102, '整改复查', 'B', NULL, NULL, 'supervisor:rectification:review', NULL, 24, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (325, 102, '告警关闭', 'B', NULL, NULL, 'supervisor:alarm:close', NULL, 18, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (326, 102, '项目详情', 'B', NULL, NULL, 'supervisor:project:query', NULL, 2, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (327, 102, '企业详情', 'B', NULL, NULL, 'supervisor:enterprise:query', NULL, 7, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (328, 102, '设备详情', 'B', NULL, NULL, 'supervisor:device:query', NULL, 11, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (401, 103, '企业项目查询', 'B', NULL, NULL, 'enterprise:project:list', NULL, 1, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (402, 103, '项目进度上报', 'B', NULL, NULL, 'enterprise:project:progress', NULL, 2, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (403, 103, '企业告警查询', 'B', NULL, NULL, 'enterprise:alarm:list', NULL, 3, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (404, 103, '整改查询', 'B', NULL, NULL, 'enterprise:rectification:list', NULL, 4, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (405, 103, '整改提交', 'B', NULL, NULL, 'enterprise:rectification:submit', NULL, 5, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (501, 104, '运营总览', 'B', NULL, NULL, 'admin:dashboard:view', NULL, 1, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (502, 104, '反馈查询', 'B', NULL, NULL, 'admin:feedback:list', NULL, 2, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component`, `permission_code`, `icon`, `sort_order`, `visible`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (503, 104, '反馈处理', 'B', NULL, NULL, 'admin:feedback:handle', NULL, 3, 1, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `module_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `operation_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `request_method` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `request_uri` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `request_params` text COLLATE utf8mb4_unicode_ci,
  `response_code` int DEFAULT NULL,
  `client_ip` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `execution_time_ms` bigint DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_operation_log_user_id` (`user_id`),
  KEY `idx_operation_log_created_at` (`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Operation audit log';

-- ----------------------------
-- Records of sys_operation_log
-- ----------------------------
BEGIN;
INSERT INTO `sys_operation_log` (`id`, `user_id`, `username`, `module_name`, `operation_name`, `request_method`, `request_uri`, `request_params`, `response_code`, `client_ip`, `execution_time_ms`, `created_at`) VALUES (1, 2, 'supervisor', '测试数据', '查看摄像头列表', 'GET', '/supervisor/camera/page', '{\"seed\":\"codex-demo-log-001\"}', 200, '127.0.0.1', 36, '2026-08-11 23:21:52');
COMMIT;

-- ----------------------------
-- Table structure for sys_organization
-- ----------------------------
DROP TABLE IF EXISTS `sys_organization`;
CREATE TABLE `sys_organization` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint NOT NULL DEFAULT '0',
  `org_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `org_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `org_type` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sort_order` int NOT NULL DEFAULT '0',
  `status` tinyint NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_org_code` (`org_code`),
  KEY `idx_org_parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Organization tree';

-- ----------------------------
-- Records of sys_organization
-- ----------------------------
BEGIN;
INSERT INTO `sys_organization` (`id`, `parent_id`, `org_name`, `org_code`, `org_type`, `sort_order`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (1, 0, '市建设工程监管中心', 'CITY_SUPERVISOR', 'SUPERVISOR', 1, 1, '2026-08-04 17:40:14', '2026-08-04 17:40:14', 0);
INSERT INTO `sys_organization` (`id`, `parent_id`, `org_name`, `org_code`, `org_type`, `sort_order`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (2, 0, '示范建设集团', 'ENTERPRISE_DEMO', 'ENTERPRISE', 2, 1, '2026-08-04 19:50:36', '2026-08-04 19:50:36', 0);
INSERT INTO `sys_organization` (`id`, `parent_id`, `org_name`, `org_code`, `org_type`, `sort_order`, `status`, `created_at`, `updated_at`, `deleted`) VALUES (3, 1, '示例施工企业', 'DEMO_ENTERPRISE', 'ENTERPRISE', 0, 1, '2026-08-10 10:16:15', '2026-08-10 10:16:15', 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `data_scope` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SELF',
  `status` tinyint NOT NULL DEFAULT '1',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注\n',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Role and data scope';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `data_scope`, `status`, `remark`, `created_at`, `updated_at`, `deleted`) VALUES (1, '系统管理员', 'ADMIN', 'ALL', 1, '系统初始化管理员角色', '2026-08-04 15:58:19', '2026-08-04 15:58:19', 0);
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `data_scope`, `status`, `remark`, `created_at`, `updated_at`, `deleted`) VALUES (2, '监管人员', 'SUPERVISOR', 'SELF', 1, '监管端项目与告警处理角色', '2026-08-04 17:40:14', '2026-08-04 17:40:14', 0);
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `data_scope`, `status`, `remark`, `created_at`, `updated_at`, `deleted`) VALUES (3, '施工企业', 'ENTERPRISE', 'SELF', 1, '施工企业端业务角色', '2026-08-04 19:50:36', '2026-08-04 19:50:36', 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL,
  `menu_id` bigint NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_menu` (`role_id`,`menu_id`),
  KEY `idx_role_menu_menu_id` (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=215 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Role menu relation';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (1, 1, 1, '2026-08-04 15:58:19');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (2, 1, 2, '2026-08-04 15:58:19');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (3, 1, 3, '2026-08-04 15:58:19');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (4, 1, 4, '2026-08-04 15:58:19');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (5, 1, 5, '2026-08-04 15:58:19');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (6, 1, 9, '2026-08-04 15:58:19');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (7, 1, 10, '2026-08-04 15:58:19');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (8, 1, 11, '2026-08-04 15:58:19');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (9, 1, 12, '2026-08-04 15:58:19');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (10, 1, 13, '2026-08-04 15:58:19');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (11, 1, 14, '2026-08-04 15:58:19');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (12, 1, 15, '2026-08-04 15:58:19');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (13, 1, 16, '2026-08-04 15:58:19');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (14, 1, 17, '2026-08-04 15:58:19');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (15, 1, 18, '2026-08-04 15:58:19');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (16, 1, 19, '2026-08-04 15:58:19');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (17, 1, 20, '2026-08-04 15:58:19');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (18, 1, 21, '2026-08-04 15:58:19');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (19, 1, 22, '2026-08-04 15:58:19');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (20, 1, 23, '2026-08-04 15:58:19');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (21, 1, 24, '2026-08-04 15:58:19');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (22, 1, 25, '2026-08-04 15:58:19');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (32, 2, 40, '2026-08-04 17:40:14');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (33, 1, 40, '2026-08-04 17:40:14');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (34, 2, 44, '2026-08-04 17:40:14');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (35, 1, 44, '2026-08-04 17:40:14');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (36, 2, 45, '2026-08-04 17:40:14');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (37, 1, 45, '2026-08-04 17:40:14');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (38, 2, 46, '2026-08-04 17:40:14');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (39, 1, 46, '2026-08-04 17:40:14');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (40, 2, 47, '2026-08-04 17:40:14');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (41, 1, 47, '2026-08-04 17:40:14');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (42, 2, 48, '2026-08-04 17:40:14');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (43, 1, 48, '2026-08-04 17:40:14');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (44, 2, 49, '2026-08-04 17:40:14');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (45, 1, 49, '2026-08-04 17:40:14');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (46, 2, 50, '2026-08-04 17:40:14');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (47, 1, 50, '2026-08-04 17:40:14');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (48, 2, 51, '2026-08-04 17:40:14');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (49, 1, 51, '2026-08-04 17:40:14');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (50, 2, 52, '2026-08-04 17:40:14');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (51, 1, 52, '2026-08-04 17:40:14');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (52, 2, 53, '2026-08-04 17:40:14');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (53, 1, 53, '2026-08-04 17:40:14');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (54, 2, 54, '2026-08-04 17:40:14');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (55, 1, 54, '2026-08-04 17:40:14');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (63, 2, 59, '2026-08-04 19:24:12');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (64, 1, 59, '2026-08-04 19:24:12');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (65, 2, 60, '2026-08-04 19:24:12');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (66, 1, 60, '2026-08-04 19:24:12');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (67, 2, 61, '2026-08-04 19:24:12');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (68, 1, 61, '2026-08-04 19:24:12');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (69, 2, 62, '2026-08-04 19:24:12');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (70, 1, 62, '2026-08-04 19:24:12');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (71, 2, 63, '2026-08-04 19:24:12');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (72, 1, 63, '2026-08-04 19:24:12');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (73, 2, 64, '2026-08-04 19:24:12');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (74, 1, 64, '2026-08-04 19:24:12');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (78, 1, 67, '2026-08-04 19:30:42');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (79, 1, 68, '2026-08-04 19:30:42');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (80, 2, 67, '2026-08-04 19:30:42');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (81, 2, 68, '2026-08-04 19:30:42');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (85, 1, 79, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (86, 1, 77, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (87, 1, 78, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (88, 1, 75, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (89, 1, 76, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (90, 1, 71, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (91, 1, 73, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (92, 1, 74, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (93, 1, 72, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (94, 1, 91, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (95, 1, 90, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (96, 1, 88, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (97, 1, 87, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (98, 1, 89, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (99, 1, 95, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (100, 1, 96, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (101, 1, 94, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (102, 3, 91, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (103, 3, 90, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (104, 3, 88, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (105, 3, 87, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (106, 3, 89, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (107, 2, 79, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (108, 2, 77, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (109, 2, 78, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (110, 2, 75, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (111, 2, 76, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (112, 2, 71, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (113, 2, 73, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (114, 2, 74, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (115, 2, 72, '2026-08-04 19:50:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (116, 1, 501, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (117, 1, 503, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (118, 1, 502, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (119, 1, 403, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (120, 1, 401, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (121, 1, 402, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (122, 1, 404, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (123, 1, 405, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (124, 1, 319, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (125, 1, 321, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (126, 1, 320, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (127, 1, 318, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (128, 1, 325, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (129, 1, 317, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (130, 1, 316, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (131, 1, 311, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (132, 1, 313, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (133, 1, 312, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (134, 1, 310, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (135, 1, 328, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (136, 1, 307, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (137, 1, 309, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (138, 1, 308, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (139, 1, 306, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (140, 1, 327, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (141, 1, 314, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (142, 1, 315, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (143, 1, 302, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (144, 1, 305, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (145, 1, 304, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (146, 1, 303, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (147, 1, 301, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (148, 1, 326, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (149, 1, 323, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (150, 1, 322, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (151, 1, 324, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (152, 1, 220, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (153, 1, 219, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (154, 1, 217, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (155, 1, 218, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (156, 1, 221, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (157, 1, 213, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (158, 1, 212, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (159, 1, 211, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (160, 1, 216, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (161, 1, 215, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (162, 1, 214, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (163, 1, 210, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (164, 1, 209, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (165, 1, 208, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (166, 1, 206, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (167, 1, 207, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (168, 1, 203, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (169, 1, 205, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (170, 1, 204, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (171, 1, 201, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (172, 1, 202, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (179, 2, 319, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (180, 2, 321, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (181, 2, 320, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (182, 2, 318, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (183, 2, 325, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (184, 2, 317, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (185, 2, 316, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (186, 2, 311, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (187, 2, 313, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (188, 2, 312, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (189, 2, 310, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (190, 2, 328, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (191, 2, 307, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (192, 2, 309, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (193, 2, 308, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (194, 2, 306, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (195, 2, 327, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (196, 2, 314, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (197, 2, 315, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (198, 2, 302, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (199, 2, 305, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (200, 2, 304, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (201, 2, 303, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (202, 2, 301, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (203, 2, 326, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (204, 2, 323, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (205, 2, 322, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (206, 2, 324, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (210, 3, 403, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (211, 3, 401, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (212, 3, 402, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (213, 3, 404, '2026-08-10 10:16:15');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `created_at`) VALUES (214, 3, 405, '2026-08-10 10:16:15');
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `organization_id` bigint NOT NULL DEFAULT '0',
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password_hash` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `real_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `mobile` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_type` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` tinyint NOT NULL DEFAULT '1',
  `last_login_at` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_username` (`username`),
  UNIQUE KEY `uk_user_mobile` (`mobile`),
  KEY `idx_user_organization_id` (`organization_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='System user';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` (`id`, `organization_id`, `username`, `password_hash`, `real_name`, `mobile`, `email`, `user_type`, `status`, `last_login_at`, `created_at`, `updated_at`, `deleted`) VALUES (1, 0, 'admin', '$2y$10$YlDam6SSXL01r4nT1B4t0unmgdeghLrORaV3B2Wg7t/Iefkp6RURC', 'System Administrator', NULL, NULL, 'ADMIN', 1, NULL, '2026-08-04 15:28:01', '2026-08-04 20:07:54', 0);
INSERT INTO `sys_user` (`id`, `organization_id`, `username`, `password_hash`, `real_name`, `mobile`, `email`, `user_type`, `status`, `last_login_at`, `created_at`, `updated_at`, `deleted`) VALUES (2, 1, 'supervisor', '$2y$10$YlDam6SSXL01r4nT1B4t0unmgdeghLrORaV3B2Wg7t/Iefkp6RURC', '监管员张伟', '13800000001', NULL, 'SUPERVISOR', 1, NULL, '2026-08-04 17:40:14', '2026-08-04 20:07:54', 0);
INSERT INTO `sys_user` (`id`, `organization_id`, `username`, `password_hash`, `real_name`, `mobile`, `email`, `user_type`, `status`, `last_login_at`, `created_at`, `updated_at`, `deleted`) VALUES (3, 2, 'enterprise', '$2y$10$YlDam6SSXL01r4nT1B4t0unmgdeghLrORaV3B2Wg7t/Iefkp6RURC', '企业项目经理', '13900000001', NULL, 'ENTERPRISE', 1, NULL, '2026-08-04 19:50:36', '2026-08-04 20:07:54', 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`,`role_id`),
  KEY `idx_user_role_role_id` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='User role relation';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`, `created_at`) VALUES (1, 1, 1, '2026-08-04 15:58:19');
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`, `created_at`) VALUES (2, 2, 2, '2026-08-04 17:40:14');
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`, `created_at`) VALUES (3, 3, 3, '2026-08-04 19:50:36');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
