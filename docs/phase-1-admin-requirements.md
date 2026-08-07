# 第四阶段：管理员端需求分析

## 模块边界

- 用户、角色、权限、组织、菜单继续由 `system` 模块负责。
- `admin-terminal` 负责一期运营总览和公众反馈处理，不复制基础数据 CRUD。

## 运营总览

- 统计项目、在线设备、待处理告警、待整改/待复查整改单和待处理公众反馈数量。
- 总览数据面向 `ADMIN`，不使用监管人员项目范围。

## 公众反馈处理

- 管理员查询全部反馈。
- 处理后记录处理人、处理时间、处理结果，状态变为 `RESOLVED`。

## 权限

- `admin:dashboard:view`
- `admin:feedback:list`、`admin:feedback:handle`
