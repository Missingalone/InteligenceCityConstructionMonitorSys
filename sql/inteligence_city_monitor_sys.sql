CREATE DATABASE IF NOT EXISTS inteligence_city_monitor_sys
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE inteligence_city_monitor_sys;

CREATE TABLE IF NOT EXISTS sys_organization (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    parent_id BIGINT NOT NULL DEFAULT 0,
    org_name VARCHAR(100) NOT NULL,
    org_code VARCHAR(50) NOT NULL,
    org_type VARCHAR(30) NOT NULL,
    sort_order INT NOT NULL DEFAULT 0,
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_org_code (org_code),
    KEY idx_org_parent_id (parent_id)
) ENGINE=InnoDB COMMENT='Organization tree';

CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    organization_id BIGINT NOT NULL DEFAULT 0,
    username VARCHAR(50) NOT NULL,
    password_hash VARCHAR(100) NOT NULL,
    real_name VARCHAR(50) NOT NULL,
    mobile VARCHAR(20) DEFAULT NULL,
    email VARCHAR(100) DEFAULT NULL,
    user_type VARCHAR(30) NOT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    last_login_at DATETIME DEFAULT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_user_username (username),
    UNIQUE KEY uk_user_mobile (mobile),
    KEY idx_user_organization_id (organization_id)
) ENGINE=InnoDB COMMENT='System user';

CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL,
    role_code VARCHAR(50) NOT NULL,
    data_scope VARCHAR(30) NOT NULL DEFAULT 'SELF',
    status TINYINT NOT NULL DEFAULT 1,
    remark VARCHAR(255) DEFAULT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_role_code (role_code)
) ENGINE=InnoDB COMMENT='Role and data scope';

CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_role (user_id, role_id),
    KEY idx_user_role_role_id (role_id)
) ENGINE=InnoDB COMMENT='User role relation';

CREATE TABLE IF NOT EXISTS sys_menu (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    parent_id BIGINT NOT NULL DEFAULT 0,
    menu_name VARCHAR(50) NOT NULL,
    menu_type CHAR(1) NOT NULL,
    route_path VARCHAR(200) DEFAULT NULL,
    component VARCHAR(200) DEFAULT NULL,
    permission_code VARCHAR(100) DEFAULT NULL,
    icon VARCHAR(50) DEFAULT NULL,
    sort_order INT NOT NULL DEFAULT 0,
    visible TINYINT NOT NULL DEFAULT 1,
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    KEY idx_menu_parent_id (parent_id),
    KEY idx_menu_permission_code (permission_code)
) ENGINE=InnoDB COMMENT='Menu and permission';

CREATE TABLE IF NOT EXISTS sys_role_menu (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_role_menu (role_id, menu_id),
    KEY idx_role_menu_menu_id (menu_id)
) ENGINE=InnoDB COMMENT='Role menu relation';

CREATE TABLE IF NOT EXISTS sys_dict_type (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dict_name VARCHAR(100) NOT NULL,
    dict_code VARCHAR(100) NOT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    remark VARCHAR(255) DEFAULT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_dict_type_code (dict_code)
) ENGINE=InnoDB COMMENT='Dictionary type';

CREATE TABLE IF NOT EXISTS sys_dict_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dict_type_id BIGINT NOT NULL,
    item_label VARCHAR(100) NOT NULL,
    item_value VARCHAR(100) NOT NULL,
    sort_order INT NOT NULL DEFAULT 0,
    status TINYINT NOT NULL DEFAULT 1,
    remark VARCHAR(255) DEFAULT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_dict_item_value (dict_type_id, item_value),
    KEY idx_dict_item_type_id (dict_type_id)
) ENGINE=InnoDB COMMENT='Dictionary item';

CREATE TABLE IF NOT EXISTS sys_operation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT DEFAULT NULL,
    username VARCHAR(50) DEFAULT NULL,
    module_name VARCHAR(100) NOT NULL,
    operation_name VARCHAR(100) NOT NULL,
    request_method VARCHAR(10) DEFAULT NULL,
    request_uri VARCHAR(500) DEFAULT NULL,
    request_params TEXT DEFAULT NULL,
    response_code INT DEFAULT NULL,
    client_ip VARCHAR(50) DEFAULT NULL,
    execution_time_ms BIGINT DEFAULT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_operation_log_user_id (user_id),
    KEY idx_operation_log_created_at (created_at)
) ENGINE=InnoDB COMMENT='Operation audit log';

CREATE TABLE IF NOT EXISTS biz_enterprise (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    organization_id BIGINT DEFAULT NULL,
    enterprise_name VARCHAR(150) NOT NULL,
    unified_social_credit_code VARCHAR(50) DEFAULT NULL,
    legal_representative VARCHAR(50) DEFAULT NULL,
    contact_name VARCHAR(50) DEFAULT NULL,
    contact_mobile VARCHAR(20) DEFAULT NULL,
    address VARCHAR(255) DEFAULT NULL,
    qualification_info VARCHAR(500) DEFAULT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_enterprise_credit_code (unified_social_credit_code)
    ,KEY idx_enterprise_organization_id (organization_id)
) ENGINE=InnoDB COMMENT='Construction enterprise';

CREATE TABLE IF NOT EXISTS biz_project (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    project_code VARCHAR(50) NOT NULL,
    project_name VARCHAR(200) NOT NULL,
    enterprise_id BIGINT NOT NULL,
    supervisor_org_id BIGINT NOT NULL DEFAULT 0,
    project_type VARCHAR(30) NOT NULL,
    project_status VARCHAR(30) NOT NULL DEFAULT 'PREPARING',
    address VARCHAR(255) DEFAULT NULL,
    longitude DECIMAL(10,7) DEFAULT NULL,
    latitude DECIMAL(10,7) DEFAULT NULL,
    planned_start_date DATE DEFAULT NULL,
    planned_end_date DATE DEFAULT NULL,
    actual_start_date DATE DEFAULT NULL,
    progress_percent DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    project_manager VARCHAR(50) DEFAULT NULL,
    manager_mobile VARCHAR(20) DEFAULT NULL,
    description TEXT DEFAULT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_project_code (project_code),
    KEY idx_project_enterprise_id (enterprise_id),
    KEY idx_project_status (project_status),
    KEY idx_project_supervisor_org_id (supervisor_org_id)
) ENGINE=InnoDB COMMENT='Construction project';

CREATE TABLE IF NOT EXISTS biz_project_member (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    project_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    member_role VARCHAR(30) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_project_member (project_id, user_id),
    KEY idx_project_member_user_id (user_id)
) ENGINE=InnoDB COMMENT='Project member';

CREATE TABLE IF NOT EXISTS biz_device (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    device_code VARCHAR(100) NOT NULL,
    device_name VARCHAR(100) NOT NULL,
    project_id BIGINT NOT NULL,
    device_type VARCHAR(30) NOT NULL,
    manufacturer VARCHAR(100) DEFAULT NULL,
    model VARCHAR(100) DEFAULT NULL,
    installation_location VARCHAR(255) DEFAULT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'OFFLINE',
    last_online_at DATETIME DEFAULT NULL,
    installed_at DATETIME DEFAULT NULL,
    remark VARCHAR(255) DEFAULT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_device_code (device_code),
    KEY idx_device_project_id (project_id),
    KEY idx_device_status (status)
) ENGINE=InnoDB COMMENT='Monitoring device';

CREATE TABLE IF NOT EXISTS construction_foundation_pit (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    pit_code VARCHAR(64) NOT NULL,
    pit_name VARCHAR(200) NOT NULL,
    project_id BIGINT NOT NULL,
    pit_type VARCHAR(50) DEFAULT NULL,
    excavation_method VARCHAR(100) DEFAULT NULL,
    area DECIMAL(12,2) DEFAULT NULL,
    support_type VARCHAR(100) DEFAULT NULL,
    support_scheme VARCHAR(500) DEFAULT NULL,
    excavation_start_date DATE DEFAULT NULL,
    excavation_end_date DATE DEFAULT NULL,
    backfill_date DATE DEFAULT NULL,
    current_stage VARCHAR(50) DEFAULT NULL,
    progress DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    risk_level VARCHAR(30) DEFAULT NULL,
    monitoring_status TINYINT NOT NULL DEFAULT 0,
    warning_threshold VARCHAR(500) DEFAULT NULL,
    alarm_reason VARCHAR(500) DEFAULT NULL,
    longitude DECIMAL(10,7) DEFAULT NULL,
    latitude DECIMAL(10,7) DEFAULT NULL,
    location_address VARCHAR(255) DEFAULT NULL,
    responsible_person VARCHAR(50) DEFAULT NULL,
    responsible_phone VARCHAR(30) DEFAULT NULL,
    description TEXT DEFAULT NULL,
    remark VARCHAR(500) DEFAULT NULL,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_foundation_pit_code (pit_code),
    KEY idx_foundation_pit_project (project_id),
    KEY idx_foundation_pit_status (monitoring_status)
) ENGINE=InnoDB COMMENT='Construction foundation pit';

CREATE TABLE IF NOT EXISTS city_camera (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    camera_code VARCHAR(64) NOT NULL,
    camera_name VARCHAR(100) NOT NULL,
    project_id BIGINT DEFAULT NULL,
    foundation_pit_id BIGINT DEFAULT NULL,
    camera_type VARCHAR(30) DEFAULT NULL,
    device_model VARCHAR(100) DEFAULT NULL,
    manufacturer VARCHAR(100) DEFAULT NULL,
    installation_address VARCHAR(255) DEFAULT NULL,
    direction VARCHAR(50) DEFAULT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    has_audio TINYINT NOT NULL DEFAULT 0,
    has_ptz TINYINT NOT NULL DEFAULT 0,
    remark VARCHAR(500) DEFAULT NULL,
    created_by BIGINT DEFAULT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT NULL,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_camera_code (camera_code),
    KEY idx_camera_project (project_id),
    KEY idx_camera_foundation_pit (foundation_pit_id),
    KEY idx_camera_status (status)
) ENGINE=InnoDB COMMENT='City construction camera';

CREATE TABLE IF NOT EXISTS biz_device_monitor_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    device_id BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    pm25 DECIMAL(10,2) DEFAULT NULL,
    pm10 DECIMAL(10,2) DEFAULT NULL,
    noise_db DECIMAL(10,2) DEFAULT NULL,
    temperature DECIMAL(10,2) DEFAULT NULL,
    humidity DECIMAL(10,2) DEFAULT NULL,
    wind_speed DECIMAL(10,2) DEFAULT NULL,
    wind_direction VARCHAR(20) DEFAULT NULL,
    raw_data JSON DEFAULT NULL,
    collected_at DATETIME NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_monitor_data_device_time (device_id, collected_at),
    KEY idx_monitor_data_project_time (project_id, collected_at)
) ENGINE=InnoDB COMMENT='Device monitoring time series';

CREATE TABLE IF NOT EXISTS biz_alarm_rule (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    rule_name VARCHAR(100) NOT NULL,
    rule_code VARCHAR(100) NOT NULL,
    device_type VARCHAR(30) DEFAULT NULL,
    metric_name VARCHAR(50) DEFAULT NULL,
    comparison_operator VARCHAR(10) NOT NULL,
    threshold_value DECIMAL(12,2) DEFAULT NULL,
    alarm_level VARCHAR(20) NOT NULL,
    enabled TINYINT NOT NULL DEFAULT 1,
    remark VARCHAR(255) DEFAULT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_alarm_rule_code (rule_code)
) ENGINE=InnoDB COMMENT='Alarm rule';

CREATE TABLE IF NOT EXISTS biz_alarm_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    alarm_no VARCHAR(50) NOT NULL,
    project_id BIGINT NOT NULL,
    device_id BIGINT DEFAULT NULL,
    rule_id BIGINT DEFAULT NULL,
    alarm_type VARCHAR(30) NOT NULL,
    alarm_level VARCHAR(20) NOT NULL,
    alarm_title VARCHAR(200) NOT NULL,
    alarm_content TEXT DEFAULT NULL,
    threshold_value DECIMAL(12,2) DEFAULT NULL,
    actual_value DECIMAL(12,2) DEFAULT NULL,
    alarm_status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    handler_id BIGINT DEFAULT NULL,
    handled_at DATETIME DEFAULT NULL,
    handle_remark VARCHAR(500) DEFAULT NULL,
    triggered_at DATETIME NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_alarm_record_no (alarm_no),
    KEY idx_alarm_project_status (project_id, alarm_status),
    KEY idx_alarm_device_id (device_id),
    KEY idx_alarm_triggered_at (triggered_at)
) ENGINE=InnoDB COMMENT='Business alarm record';

CREATE TABLE IF NOT EXISTS biz_rectification_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(50) NOT NULL,
    project_id BIGINT NOT NULL,
    alarm_id BIGINT DEFAULT NULL,
    enterprise_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    deadline_at DATETIME NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    issued_by BIGINT NOT NULL,
    issued_at DATETIME NOT NULL,
    submitted_by BIGINT DEFAULT NULL,
    submitted_at DATETIME DEFAULT NULL,
    result_description TEXT DEFAULT NULL,
    evidence_urls JSON DEFAULT NULL,
    reviewed_by BIGINT DEFAULT NULL,
    reviewed_at DATETIME DEFAULT NULL,
    review_remark VARCHAR(500) DEFAULT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_rectification_order_no (order_no),
    KEY idx_rectification_project_status (project_id, status),
    KEY idx_rectification_enterprise_id (enterprise_id),
    KEY idx_rectification_deadline_at (deadline_at)
) ENGINE=InnoDB COMMENT='Rectification workflow';

CREATE TABLE IF NOT EXISTS biz_public_feedback (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    feedback_no VARCHAR(50) NOT NULL,
    project_id BIGINT DEFAULT NULL,
    feedback_type VARCHAR(30) NOT NULL,
    content TEXT NOT NULL,
    contact_name VARCHAR(50) DEFAULT NULL,
    contact_mobile VARCHAR(20) DEFAULT NULL,
    attachment_urls JSON DEFAULT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    handler_id BIGINT DEFAULT NULL,
    handled_at DATETIME DEFAULT NULL,
    handle_result VARCHAR(1000) DEFAULT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_public_feedback_no (feedback_no),
    KEY idx_public_feedback_project_id (project_id),
    KEY idx_public_feedback_status (status)
) ENGINE=InnoDB COMMENT='Public feedback';

-- ============================================================
-- 基础权限初始化数据
-- 说明：以下 INSERT 使用固定业务编码并配合 INSERT IGNORE，脚本可重复执行。
-- 初始化账号密码统一为 password，部署后必须立即修改密码。
-- BCrypt 哈希由 Spring Security BCryptPasswordEncoder 生成。
-- ============================================================

INSERT IGNORE INTO sys_organization (id, parent_id, org_name, org_code, org_type)
VALUES
    (1, 0, '智慧城市监测中心', 'CITY_MONITOR_CENTER', 'SUPERVISOR'),
    (2, 1, '城市监管处', 'CITY_SUPERVISION', 'SUPERVISOR'),
    (3, 1, '示例施工企业', 'DEMO_ENTERPRISE', 'ENTERPRISE');

INSERT IGNORE INTO sys_role (id, role_name, role_code, data_scope, status)
VALUES
    (1, '系统管理员', 'ADMIN', 'ALL', 1),
    (2, '监管人员', 'SUPERVISOR', 'PROJECT', 1),
    (3, '施工企业用户', 'ENTERPRISE', 'ORG', 1);

-- 菜单表同时承担按钮权限表的职责，菜单类型 B 表示按钮权限。
INSERT IGNORE INTO sys_menu (id, parent_id, menu_name, menu_type, permission_code, sort_order)
VALUES
    (101,0,'系统管理','M',NULL,1),
    (102,0,'监管业务','M',NULL,2),
    (103,0,'企业工作台','M',NULL,3),
    (104,0,'管理员运营','M',NULL,4),
    (201,101,'用户管理','B','system:user:list',1),
    (202,101,'用户查询','B','system:user:query',2),
    (203,101,'用户新增','B','system:user:add',3),
    (204,101,'用户编辑','B','system:user:edit',4),
    (205,101,'用户删除','B','system:user:delete',5),
    (206,101,'角色列表','B','system:role:list',6),
    (207,101,'角色查询','B','system:role:query',7),
    (208,101,'角色编辑','B','system:role:edit',8),
    (209,101,'角色删除','B','system:role:delete',9),
    (210,101,'角色授权','B','system:role:authorize',10),
    (211,101,'菜单管理','B','system:menu:list',11),
    (212,101,'菜单编辑','B','system:menu:edit',12),
    (213,101,'菜单删除','B','system:menu:delete',13),
    (214,101,'组织管理','B','system:organization:list',14),
    (215,101,'组织编辑','B','system:organization:edit',15),
    (216,101,'组织删除','B','system:organization:delete',16),
    (217,101,'字典管理','B','system:dict:list',17),
    (218,101,'字典查询','B','system:dict:query',18),
    (219,101,'字典编辑','B','system:dict:edit',19),
    (220,101,'字典删除','B','system:dict:delete',20),
    (221,101,'操作日志','B','system:log:list',21),
    (301,102,'项目查询','B','supervisor:project:list',1),
    (326,102,'项目详情','B','supervisor:project:query',2),
    (302,102,'项目操作','B','supervisor:project:add',2),
    (303,102,'项目编辑','B','supervisor:project:edit',3),
    (304,102,'项目删除','B','supervisor:project:delete',4),
    (305,102,'成员分配','B','supervisor:project:assign-member',5),
    (306,102,'企业查询','B','supervisor:enterprise:list',6),
    (327,102,'企业详情','B','supervisor:enterprise:query',7),
    (307,102,'企业操作','B','supervisor:enterprise:add',7),
    (308,102,'企业编辑','B','supervisor:enterprise:edit',8),
    (309,102,'企业删除','B','supervisor:enterprise:delete',9),
    (310,102,'设备查询','B','supervisor:device:list',10),
    (328,102,'设备详情','B','supervisor:device:query',11),
    (311,102,'设备操作','B','supervisor:device:add',11),
    (312,102,'设备编辑','B','supervisor:device:edit',12),
    (313,102,'设备删除','B','supervisor:device:delete',13),
    (314,102,'监测数据查询','B','supervisor:monitor:list',14),
    (315,102,'监测数据上报','B','supervisor:monitor:report',15),
    (316,102,'告警查询','B','supervisor:alarm:list',16),
    (317,102,'告警处理','B','supervisor:alarm:handle',17),
    (325,102,'告警关闭','B','supervisor:alarm:close',18),
    (318,102,'告警规则查询','B','supervisor:alarm-rule:list',18),
    (319,102,'告警规则新增','B','supervisor:alarm-rule:add',19),
    (320,102,'告警规则编辑','B','supervisor:alarm-rule:edit',20),
    (321,102,'告警规则删除','B','supervisor:alarm-rule:delete',21),
    (322,102,'整改查询','B','supervisor:rectification:list',22),
    (323,102,'整改下发','B','supervisor:rectification:issue',23),
    (324,102,'整改复查','B','supervisor:rectification:review',24),
    (401,103,'企业项目查询','B','enterprise:project:list',1),
    (402,103,'项目进度上报','B','enterprise:project:progress',2),
    (403,103,'企业告警查询','B','enterprise:alarm:list',3),
    (404,103,'整改查询','B','enterprise:rectification:list',4),
    (405,103,'整改提交','B','enterprise:rectification:submit',5),
    (501,104,'运营总览','B','admin:dashboard:view',1),
    (502,104,'反馈查询','B','admin:feedback:list',2),
    (503,104,'反馈处理','B','admin:feedback:handle',3);

-- 系统管理员拥有所有权限，监管人员和企业用户按职责最小授权。
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT 1, id FROM sys_menu WHERE permission_code IS NOT NULL;
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT 2, id FROM sys_menu WHERE permission_code LIKE 'supervisor:%';
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT 3, id FROM sys_menu WHERE permission_code LIKE 'enterprise:%';

-- password 字符串的 BCrypt 哈希，仅用于本地初始化，生产环境请改密。
INSERT IGNORE INTO sys_user (id, organization_id, username, password_hash, real_name, user_type)
VALUES
    (1, 1, 'admin', '$2a$10$X9RnXAdLxtbNQlEOx8xQUOPf7qfNotB/B66xxzf7eumiWnlauE2g.', '系统管理员', 'ADMIN'),
    (2, 2, 'supervisor', '$2a$10$X9RnXAdLxtbNQlEOx8xQUOPf7qfNotB/B66xxzf7eumiWnlauE2g.', '监管人员', 'SUPERVISOR'),
    (3, 3, 'enterprise', '$2a$10$X9RnXAdLxtbNQlEOx8xQUOPf7qfNotB/B66xxzf7eumiWnlauE2g.', '企业用户', 'ENTERPRISE');

INSERT IGNORE INTO sys_user_role (user_id, role_id)
VALUES (1,1), (2,2), (3,3);

-- 企业账号的数据范围依赖企业与组织的关联，这条初始化数据保证 enterprise 账号可正确完成归属校验。
INSERT IGNORE INTO biz_enterprise (id, organization_id, enterprise_name, unified_social_credit_code)
VALUES (1, 3, '示例施工企业', 'DEMO-UNIFIED-CREDIT-CODE');
