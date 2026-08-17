import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import { getToken } from '@/api/http'
import { hasPermission } from '@/api/permission'

const routes: RouteRecordRaw[] = [
  { path: '/login', name: 'login', component: () => import('@/views/Login.vue'), meta: { title: '登录', public: true } },
  { path: '/', name: 'dashboard', component: () => import('@/views/Dashboard.vue'), meta: { title: '城市安全总览' } },
  { path: '/projects', name: 'projects', component: () => import('@/views/Projects.vue'), meta: { title: '施工项目管理', permission: 'supervisor:project:list' } },
  { path: '/cameras', name: 'cameras', component: () => import('@/views/Cameras.vue'), meta: { title: '摄像头管理' } },
  { path: '/foundation-pits', name: 'foundation-pits', component: () => import('@/views/FoundationPits.vue'), meta: { title: '基坑管理' } },
  { path: '/alarms', name: 'alarms', component: () => import('@/views/Alarms.vue'), meta: { title: '统一报警中心', permission: 'supervisor:alarm:list' } },
  { path: '/equipment', name: 'equipment', component: () => import('@/views/Devices.vue'), meta: { title: '设备管理', permission: 'supervisor:device:list' } },
  { path: '/maintenance', name: 'maintenance', component: () => import('@/views/Maintenance.vue'), meta: { title: '设备维护管理' } },
  { path: '/monitor-config', name: 'monitor-config', component: () => import('@/views/MonitorConfig.vue'), meta: { title: '监测配置', permission: 'supervisor:alarm-rule:list' } },
  { path: '/equipment-overview', name: 'equipment-overview', component: () => import('@/views/Equipment.vue'), meta: { title: '施工设备监控' } },
  { path: '/tower-monitor', name: 'tower-monitor', component: () => import('@/views/TowerMonitor.vue'), meta: { title: '塔吊监测分析' } },
  { path: '/environment', name: 'environment', component: () => import('@/views/Environment.vue'), meta: { title: '环境监测' } },
  { path: '/video', name: 'video', component: () => import('@/views/Video.vue'), meta: { title: '视频监控' } },
  { path: '/:pathMatch(.*)*', redirect: '/' },
]

const router = createRouter({ history: createWebHistory(), routes })
router.beforeEach((to) => {
  if (to.meta.public) return getToken() ? '/' : true
  if (!getToken()) return { path: '/login', query: { redirect: to.fullPath } }
  if (!hasPermission(to.meta.permission as string | undefined)) return '/'
  return true
})
router.afterEach((to) => { document.title = `${String(to.meta.title)}｜智慧城市建设安全监控平台` })

export default router
