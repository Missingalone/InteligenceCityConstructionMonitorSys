# 第二阶段：施工企业端需求分析

## 数据范围

- 企业账号通过 `sys_user.organization_id` 关联施工企业并不可靠，因此一期在 `biz_enterprise` 增加 `organization_id` 关联字段。
- 企业用户只能访问所属企业的项目、告警和整改单。
- 所有查询和更新均在服务端校验企业归属，不能依赖前端传入企业编号。

## 功能

- 查询本企业项目及进度，更新项目进度和实际开工日期。
- 查询本企业项目产生的告警。
- 查询本企业整改单，提交整改说明和佐证材料。

## 状态规则

- 项目进度范围为 0 到 100。
- 仅 `PENDING` 或 `REJECTED` 整改单允许企业提交。
- 提交后状态变为 `SUBMITTED`，等待监管人员复查。

## 权限

- `enterprise:project:list`、`enterprise:project:progress`
- `enterprise:alarm:list`
- `enterprise:rectification:list`、`enterprise:rectification:submit`
