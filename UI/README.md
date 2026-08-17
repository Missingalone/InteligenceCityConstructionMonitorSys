# 智慧城市建设安全智能监控平台

基于 Vue 3、TypeScript、Vite、Element Plus、Tailwind CSS、ECharts、Pinia 与 Vue Router 构建的智慧城市施工安全可视化前端。

## 页面

- `/` 城市建设安全态势总览
- `/equipment` 施工设备数字孪生监控
- `/tower-monitor` 塔吊监测分析
- `/environment` 施工环境智能监测
- `/video` 施工现场视频监控

## 本地运行

```bash
npm install
npm run dev
```

生产构建：

```bash
npm run build
```

当前已接入认证服务（8003）和监管端服务（8004）。开发环境通过 Vite `/api` 代理转发请求，登录成功后自动在请求头添加 Bearer Token。

联调前请先启动 `auth`、`Supervisor-Terminal` 和 MySQL，并确保数据库已执行后端项目中的初始化 SQL。
