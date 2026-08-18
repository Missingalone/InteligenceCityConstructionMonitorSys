# 前后端接口使用映射（2026-08-11）

## 前端现状与重构结构

- 技术栈：Vue 3、TypeScript、Vite、Vue Router、Pinia、Element Plus、ECharts、Axios。
- 请求入口：`src/api/http.ts`，统一 Bearer Token、业务错误和 401 退出处理。
- 登录与权限：登录由 Spring Security `formLogin` 的 `/auth/login` 提供；前端解析 JWT 中的 `roles`、`permissions` 控制菜单、按钮和路由。
- 保留页面：`Login.vue`、`Environment.vue`、`Video.vue`、`Equipment.vue`、`TowerMonitor.vue`。
- 新业务页面：驾驶舱、项目/企业、摄像头、基坑、统一报警/整改、设备、维护、监测配置。

## 监管端接口映射

| 后端接口 | 前端 API 方法 | 页面 | 组件 | 用户入口/状态 |
|---|---|---|---|---|
| GET `/supervisor/projects` | `getProjectList` | 首页、项目管理、设备管理 | `DashboardOverview`、`ProjectList`、`DeviceList` | 顶部“项目管理”、项目筛选、项目选择器 |
| GET `/supervisor/projects/{id}` | `getProjectDetail` | 项目管理 | `ProjectList` | “详情”按钮 |
| POST `/supervisor/projects` | `createProject` | 项目管理 | `ProjectList` | “新增项目”表单 |
| PUT `/supervisor/projects` | `updateProject` | 项目管理 | `ProjectList` | “修改”表单 |
| DELETE `/supervisor/projects/{id}` | `deleteProject` | 项目管理 | `ProjectList` | “删除”+二次确认 |
| PUT `/supervisor/projects/{id}/members` | `replaceProjectMembers` | 项目详情 | 预留 | 后端接口已封装；需项目成员候选用户查询策略后开放编辑入口 |
| GET `/supervisor/enterprises` | `getEnterpriseList` | 项目管理 | `EnterpriseList`、`ProjectList` | “施工企业”页签、项目表单企业选择 |
| GET `/supervisor/enterprises/{id}` | `getEnterpriseDetail` | 企业详情 | 预留 | API 已封装；当前列表返回字段已满足详情展示 |
| POST `/supervisor/enterprises` | `createEnterprise` | 项目管理 | `EnterpriseList` | “新增企业” |
| PUT `/supervisor/enterprises` | `updateEnterprise` | 项目管理 | `EnterpriseList` | “修改” |
| DELETE `/supervisor/enterprises/{id}` | `deleteEnterprise` | 项目管理 | `EnterpriseList` | “删除”+二次确认 |
| GET `/supervisor/camera/page` | `getCameraPage` / `getCameraList` | 首页、摄像头、塔吊视频 | `DashboardOverview`、`CameraList`、`VideoMonitorDialog` | 摄像头菜单、分页、视频入口 |
| GET `/supervisor/camera/details/{id}` | `getCameraDetail` | 摄像头管理 | `CameraList` | “详情” |
| DELETE `/supervisor/camera/{id}` | `deleteCamera` | 摄像头管理 | `CameraList` | “删除”+二次确认 |
| GET `/supervisor/devices` | `getDeviceRecords` / `getTowerDeviceList` | 首页、设备管理、塔吊监测 | `DashboardOverview`、`DeviceList`、`TowerDeviceSelector` | 设备菜单、塔吊设备切换 |
| GET `/supervisor/devices/{id}` | `getDeviceDetail` / `getTowerDeviceDetail` | 设备管理、塔吊监测 | `DeviceList`、`DeviceInfoCard` | “详情”、塔吊档案 |
| POST `/supervisor/devices` | `createDevice` | 设备管理 | `DeviceList` | “新增设备” |
| PUT `/supervisor/devices` | `updateDevice` | 设备管理 | `DeviceList` | “修改” |
| DELETE `/supervisor/devices/{id}` | `deleteDevice` | 设备管理 | `DeviceList` | “停用”+二次确认 |
| GET `/supervisor/alarms` | `getAlarms` / `getTowerAlarmList` | 首页、报警中心、塔吊监测 | `DashboardOverview`、`AlarmCenter` | 最新报警、筛选、塔吊报警 |
| PUT `/supervisor/alarms/{id}/handle` | `handleAlarm` / `confirmTowerAlarm` | 报警中心、塔吊监测 | `AlarmDetailDialog` | 标记处理中/已处理并填写意见 |
| PUT `/supervisor/alarms/{id}/close` | `closeAlarm` | 报警中心 | `AlarmDetailDialog` | 已解决报警“关闭” |
| GET `/supervisor/alarm-rules` | `getAlarmRules` | 监测配置 | `AlarmRuleList` | 顶部“监测配置” |
| POST `/supervisor/alarm-rules` | `createAlarmRule` | 监测配置 | `AlarmRuleList` | “新增规则” |
| PUT `/supervisor/alarm-rules` | `updateAlarmRule` | 监测配置 | `AlarmRuleList` | “修改” |
| DELETE `/supervisor/alarm-rules/{id}` | `deleteAlarmRule` | 监测配置 | `AlarmRuleList` | “删除”+二次确认 |
| GET `/supervisor/monitor-data` | `getMonitorData` / `getEnvironmentLatest` | 环境监测、旧驾驶舱数据层 | `Environment.vue` | 环境实时数据 |
| POST `/supervisor/monitor-data/report` | 无用户操作 API | 无 | 网关接口 | 设备/网关采样上报接口，不应由监管人员页面手工调用 |
| GET `/supervisor/rectifications` | `getRectifications` | 报警中心 | `AlarmCenter` | “整改通知”页签 |
| POST `/supervisor/rectifications` | `issueRectification` | 报警中心 | `AlarmCenter` | 报警行“下发整改” |
| PUT `/supervisor/rectifications/{id}/review` | `reviewRectification` | 报警中心 | `AlarmCenter` | 整改单“复查” |

## 认证与系统接口

| 后端接口 | 使用情况 |
|---|---|
| POST `/auth/login`（Spring Security formLogin） | `login` → `Login.vue` 登录按钮 |
| PUT `/auth/account/password` | `changePassword` → 顶部设置按钮“修改密码” |
| GET `/system/operation-logs` | `getSystemOperationLogs` → 塔吊页“操作日志”弹窗 |
| `/system/users/**`、`/system/roles/**`、`/system/menus/**`、`/system/organizations/**`、`/system/dict-types/**` | 属于独立系统管理后台；当前监管端依据 JWT 权限控制菜单，不在监管业务页面中提供系统主数据写操作 |

## 其他终端接口

- `/public/**`：公众门户专用，无需登录，不应混入监管端。
- `/enterprise/**`：施工企业工作台专用，角色和数据边界不同，不应混入监管端。
- `/admin/feedback/**`：平台运营管理员专用，当前监管端不承载公众反馈处理。
- `/admin/dashboard`：管理员一期统计口径只有项目、在线设备、待处理告警、整改、反馈；当前监管驾驶舱直接聚合监管端真实资源，避免普通监管账号跨服务 403。

## 后端缺失能力

1. 基坑：只有 `ConstructionFoundationPit` 实体，没有 Controller、Service、DTO、VO、分页 CRUD、监测点、历史数据和导出接口。
2. 维护：没有维修、保养、检查、年检、报告和到期提醒接口。
3. 摄像头：缺少新增、修改、停用、流地址/播放鉴权、最后在线时间、经纬度接口；列表 VO 也没有 `projectId`、`foundationPitId`，只有详情返回关联 ID。
4. 塔吊：监测数据 VO 只有环境字段，没有吊重、力矩、高度、幅度、倾斜、回转角度和工作状态；缺少历史时间范围查询和导出接口。
5. 报警：没有单条详情接口、`MISREPORT/误报` 状态、关联摄像头 ID、处理人信息；现有接口支持 `PENDING/HANDLING/RESOLVED/CLOSED`。
6. 项目：列表是全量 `List`，没有服务端分页和名称/编号/类型/状态筛选参数；前端当前执行客户端分页筛选。
7. 设备：列表是全量 `List`，没有服务端分页；没有独立“停用”接口，当前删除为后端逻辑删除。
8. 地图：项目有经纬度；设备、摄像头缺少统一坐标字段和聚合点位接口。

