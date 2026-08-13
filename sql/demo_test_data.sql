USE inteligence_city_monitor_sys;

START TRANSACTION;

-- 项目、成员和设备：编号统一使用 DEMO_ 前缀，便于识别和清理。
INSERT INTO biz_project
    (project_code, project_name, enterprise_id, supervisor_org_id, project_type, project_status,
     address, longitude, latitude, planned_start_date, planned_end_date, actual_start_date,
     progress_percent, project_manager, manager_mobile, description)
VALUES
    ('DEMO_PRJ_001', '智慧新城一期示范项目', 1, 1, 'HOUSING', 'CONSTRUCTING',
     '上海市浦东新区示范路88号', 121.5441200, 31.2213100, '2026-01-10', '2027-06-30', '2026-01-15',
     42.50, '张工', '13800001001', '智慧城市监测系统演示项目'),
    ('DEMO_PRJ_002', '城市轨道交通安全示范工程', 1, 1, 'MUNICIPAL', 'CONSTRUCTING',
     '上海市徐汇区建设路66号', 121.4365200, 31.1884300, '2026-03-01', '2028-02-28', '2026-03-08',
     18.00, '李工', '13800001002', '基坑与环境监测演示项目')
ON DUPLICATE KEY UPDATE project_name = VALUES(project_name), deleted = 0;

SET @project1 = (SELECT id FROM biz_project WHERE project_code = 'DEMO_PRJ_001' LIMIT 1);
SET @project2 = (SELECT id FROM biz_project WHERE project_code = 'DEMO_PRJ_002' LIMIT 1);

INSERT IGNORE INTO biz_project_member (project_id, user_id, member_role)
VALUES (@project1, 2, 'SUPERVISOR'), (@project1, 3, 'ENTERPRISE'),
       (@project2, 2, 'SUPERVISOR'), (@project2, 3, 'ENTERPRISE');

INSERT INTO biz_device
    (device_code, device_name, project_id, device_type, manufacturer, model,
     installation_location, status, last_online_at, installed_at, remark)
VALUES
    ('DEMO_ENV_001', '一期扬尘环境监测站', @project1, 'DUST', '城市感知科技', 'ENV-Pro X1',
     '项目东门', 'ONLINE', NOW(), '2026-01-16 09:00:00', '演示环境设备'),
    ('DEMO_TOWER_001', '1号塔吊', @project1, 'TOWER_CRANE', '中联重科', 'TC6013',
     '1号楼南侧', 'ONLINE', NOW(), '2026-01-20 10:00:00', '演示塔吊'),
    ('DEMO_ENV_002', '轨交项目环境监测站', @project2, 'DUST', '城市感知科技', 'ENV-Pro X2',
     '基坑北侧', 'ALARM', DATE_SUB(NOW(), INTERVAL 5 MINUTE), '2026-03-09 08:30:00', '演示报警设备'),
    ('DEMO_ELEVATOR_001', '1号施工升降机', @project2, 'ELEVATOR', '上海建机', 'SC200',
     '施工区西侧', 'OFFLINE', DATE_SUB(NOW(), INTERVAL 2 HOUR), '2026-03-12 14:00:00', '演示离线设备')
ON DUPLICATE KEY UPDATE device_name = VALUES(device_name), project_id = VALUES(project_id), deleted = 0;

SET @env1 = (SELECT id FROM biz_device WHERE device_code = 'DEMO_ENV_001' LIMIT 1);
SET @tower1 = (SELECT id FROM biz_device WHERE device_code = 'DEMO_TOWER_001' LIMIT 1);
SET @env2 = (SELECT id FROM biz_device WHERE device_code = 'DEMO_ENV_002' LIMIT 1);

INSERT INTO biz_device_monitor_data
    (device_id, project_id, pm25, pm10, noise_db, temperature, humidity, wind_speed,
     wind_direction, raw_data, collected_at)
SELECT @env1, @project1, 35.20, 62.80, 58.40, 27.60, 61.20, 2.80,
       '东南', JSON_OBJECT('seed','codex-demo-env-001'), DATE_SUB(NOW(), INTERVAL 10 MINUTE)
WHERE NOT EXISTS (SELECT 1 FROM biz_device_monitor_data
                  WHERE JSON_UNQUOTE(JSON_EXTRACT(raw_data, '$.seed')) = 'codex-demo-env-001');
INSERT INTO biz_device_monitor_data
    (device_id, project_id, pm25, pm10, noise_db, temperature, humidity, wind_speed,
     wind_direction, raw_data, collected_at)
SELECT @env2, @project2, 92.60, 138.40, 76.50, 30.10, 55.80, 5.60,
       '西北', JSON_OBJECT('seed','codex-demo-env-002'), DATE_SUB(NOW(), INTERVAL 5 MINUTE)
WHERE NOT EXISTS (SELECT 1 FROM biz_device_monitor_data
                  WHERE JSON_UNQUOTE(JSON_EXTRACT(raw_data, '$.seed')) = 'codex-demo-env-002');

-- 规则、告警和整改闭环。
INSERT INTO biz_alarm_rule
    (rule_name, rule_code, device_type, metric_name, comparison_operator, threshold_value, alarm_level, enabled, remark)
VALUES
    ('PM2.5超限预警', 'DEMO_RULE_PM25', 'DUST', 'pm25', 'GT', 75.00, 'HIGH', 1, '演示规则'),
    ('噪声超限预警', 'DEMO_RULE_NOISE', 'DUST', 'noiseDb', 'GT', 70.00, 'MEDIUM', 1, '演示规则'),
    ('大风塔吊预警', 'DEMO_RULE_WIND', 'TOWER_CRANE', 'windSpeed', 'GTE', 10.80, 'CRITICAL', 1, '演示规则')
ON DUPLICATE KEY UPDATE rule_name = VALUES(rule_name), enabled = 1, deleted = 0;

SET @pm25rule = (SELECT id FROM biz_alarm_rule WHERE rule_code = 'DEMO_RULE_PM25' LIMIT 1);
SET @noiserule = (SELECT id FROM biz_alarm_rule WHERE rule_code = 'DEMO_RULE_NOISE' LIMIT 1);

INSERT INTO biz_alarm_record
    (alarm_no, project_id, device_id, rule_id, alarm_type, alarm_level, alarm_title,
     alarm_content, threshold_value, actual_value, alarm_status, triggered_at)
VALUES
    ('DEMO_ALARM_001', @project2, @env2, @pm25rule, 'DEVICE_THRESHOLD', 'HIGH',
     '轨交项目PM2.5超限', 'PM2.5实际值92.60，阈值75.00', 75.00, 92.60, 'PENDING', DATE_SUB(NOW(), INTERVAL 5 MINUTE)),
    ('DEMO_ALARM_002', @project2, @env2, @noiserule, 'DEVICE_THRESHOLD', 'MEDIUM',
     '轨交项目噪声超限', '噪声实际值76.50dB，阈值70.00dB', 70.00, 76.50, 'HANDLING', DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
    ('DEMO_ALARM_003', @project1, @tower1, NULL, 'DEVICE_OFFLINE', 'LOW',
     '塔吊通信短暂中断', '设备曾发生短暂离线，现已恢复', NULL, NULL, 'RESOLVED', DATE_SUB(NOW(), INTERVAL 1 DAY))
ON DUPLICATE KEY UPDATE alarm_title = VALUES(alarm_title), deleted = 0;

SET @alarm1 = (SELECT id FROM biz_alarm_record WHERE alarm_no = 'DEMO_ALARM_001' LIMIT 1);
INSERT INTO biz_rectification_order
    (order_no, project_id, alarm_id, enterprise_id, title, content, deadline_at,
     status, issued_by, issued_at)
VALUES
    ('DEMO_RECT_001', @project2, @alarm1, 1, '轨交项目扬尘超限整改',
     '立即开启喷淋并检查裸土覆盖，整改完成后上传现场佐证。', DATE_ADD(NOW(), INTERVAL 2 DAY),
     'PENDING', 2, NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), deleted = 0;

-- 基坑和摄像头。
INSERT INTO construction_foundation_pit
    (pit_code, pit_name, project_id, pit_type, excavation_method, area, support_type,
     support_scheme, excavation_start_date, excavation_end_date, current_stage, progress,
     risk_level, monitoring_status, warning_threshold, longitude, latitude, location_address,
     responsible_person, responsible_phone, description, remark, created_by, updated_by)
VALUES
    ('DEMO_PIT_001', '轨交车站主体基坑', @project2, '地铁', '分层开挖', 8650.00, '地下连续墙',
     '地下连续墙+三道钢支撑', '2026-04-01', '2027-01-31', '支护施工', 28.50,
     '高', 1, '围护墙水平位移累计30mm，日变化3mm', 121.4367300, 31.1882500,
     '轨交项目施工区中央', '王工', '13800002001', '深基坑安全监测示范点', '演示基坑', 2, 2),
    ('DEMO_PIT_002', '一期地下室基坑', @project1, '地下室', '支护开挖', 3200.00, '土钉墙',
     '放坡结合土钉墙支护', '2026-02-01', '2026-08-31', '基础施工', 68.00,
     '中', 0, '坡顶位移累计25mm', 121.5443600, 31.2211500,
     '一期项目2号楼区域', '赵工', '13800002002', '地下室基坑监测点', '演示基坑', 2, 2)
ON DUPLICATE KEY UPDATE pit_name = VALUES(pit_name), deleted = 0;

SET @pit1 = (SELECT id FROM construction_foundation_pit WHERE pit_code = 'DEMO_PIT_001' LIMIT 1);
SET @pit2 = (SELECT id FROM construction_foundation_pit WHERE pit_code = 'DEMO_PIT_002' LIMIT 1);

INSERT INTO city_camera
    (camera_code, camera_name, project_id, foundation_pit_id, camera_type, device_model,
     manufacturer, installation_address, direction, status, has_audio, has_ptz, remark,
     created_by, updated_by)
VALUES
    ('DEMO_CAM_001', '轨交基坑全景摄像头', @project2, @pit1, '全景摄像头', 'IPC-Pano-8K',
     '海康威视', '基坑北侧塔架', '面向基坑中心', 1, 1, 1, '演示在线摄像头', 2, 2),
    ('DEMO_CAM_002', '地下室基坑枪机', @project1, @pit2, '枪机', 'IPC-Bullet-4M',
     '大华股份', '2号楼东南角', '面向出入口', 1, 0, 0, '演示固定摄像头', 2, 2),
    ('DEMO_CAM_003', '塔吊吊钩摄像头', @project1, NULL, '球机', 'IPC-PTZ-4M',
     '宇视科技', '1号塔吊驾驶室下方', '跟随吊钩', 0, 0, 1, '演示离线摄像头', 2, 2)
ON DUPLICATE KEY UPDATE camera_name = VALUES(camera_name), deleted = 0;

-- 公众反馈、字典和操作日志。
INSERT INTO biz_public_feedback
    (feedback_no, project_id, feedback_type, content, contact_name, contact_mobile,
     attachment_urls, status)
VALUES
    ('DEMO_FEEDBACK_001', @project1, 'NOISE', '夜间施工噪声较大，请核查施工时段。',
     '周先生', '13900003001', JSON_ARRAY('/demo/feedback/noise-001.jpg'), 'PENDING'),
    ('DEMO_FEEDBACK_002', @project2, 'DUST', '道路扬尘明显，建议增加洒水频次。',
     '陈女士', '13900003002', JSON_ARRAY(), 'PROCESSING')
ON DUPLICATE KEY UPDATE content = VALUES(content), deleted = 0;

INSERT INTO sys_dict_type (dict_name, dict_code, status, remark)
VALUES ('设备运行状态', 'DEMO_DEVICE_STATUS', 1, '演示字典')
ON DUPLICATE KEY UPDATE dict_name = VALUES(dict_name), deleted = 0;
SET @dict = (SELECT id FROM sys_dict_type WHERE dict_code = 'DEMO_DEVICE_STATUS' LIMIT 1);
INSERT IGNORE INTO sys_dict_item (dict_type_id, item_label, item_value, sort_order, status, remark)
VALUES (@dict, '在线', 'ONLINE', 1, 1, '演示字典项'),
       (@dict, '离线', 'OFFLINE', 2, 1, '演示字典项'),
       (@dict, '报警', 'ALARM', 3, 1, '演示字典项');

INSERT INTO sys_operation_log
    (user_id, username, module_name, operation_name, request_method, request_uri,
     request_params, response_code, client_ip, execution_time_ms)
SELECT 2, 'supervisor', '测试数据', '查看摄像头列表', 'GET', '/supervisor/camera/page',
       '{"seed":"codex-demo-log-001"}', 200, '127.0.0.1', 36
WHERE NOT EXISTS (SELECT 1 FROM sys_operation_log
                  WHERE request_params = '{"seed":"codex-demo-log-001"}');

COMMIT;
