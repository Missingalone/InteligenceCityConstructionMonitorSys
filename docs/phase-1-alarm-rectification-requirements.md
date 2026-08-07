# 第一阶段：告警与整改闭环需求分析

## 目标

将设备监测数据转换为可处理的告警，并形成监管下发、企业整改、监管复查的闭环。

## 告警规则

- 规则按设备类型和指标名称匹配，例如 `DUST + pm10`。
- 支持 `GT`、`GTE`、`LT`、`LTE`、`EQ` 五种比较运算。
- 规则仅在 `enabled=1` 时生效。
- 同一设备、同一规则存在未关闭告警时不重复创建，避免连续上报导致告警泛滥。

## 告警记录

- 告警状态：`PENDING`（待处理）、`HANDLING`（处理中）、`RESOLVED`（已处理）、`CLOSED`（已关闭）。
- 监管人员只能查询和处理自己负责项目的告警；管理员可操作全部告警。
- 处理动作必须记录处理人、处理时间和处理说明。

## 整改闭环

- 监管人员从告警创建整改通知单，指定施工企业和整改期限。
- 整改状态：`PENDING`（待整改）、`SUBMITTED`（待复查）、`APPROVED`（验收通过）、`REJECTED`（驳回整改）。
- 企业提交整改结果和佐证材料；监管人员复查通过或驳回。
- 整改单与原始告警通过 `alarm_id` 关联，项目和企业信息不可跨项目篡改。

## 权限

- `supervisor:alarm:list`、`supervisor:alarm:handle`
- `supervisor:alarm-rule:list/add/edit/delete`
- `supervisor:rectification:list/issue/review`
- 企业端整改提交权限在下一阶段实现。
