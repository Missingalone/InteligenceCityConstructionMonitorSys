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
