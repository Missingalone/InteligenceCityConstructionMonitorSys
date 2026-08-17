<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Bell, Building2, Camera, ChevronDown, Construction, HardHat, Menu, Settings, ShieldAlert, ShieldCheck, UserRound, Wrench, X } from 'lucide-vue-next'
import { useMonitorStore } from '@/store/monitor'
import { clearToken } from '@/api/http'
import { hasPermission } from '@/api/permission'
import { changePassword } from '@/api/system'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const store = useMonitorStore()
const mobileOpen = ref(false)
const passwordDialog = ref(false)
const changingPassword = ref(false)
const passwordForm = ref({ oldPassword: '', newPassword: '' })

const primaryMenus = [
  { label: '首页', path: '/' },
  { label: '项目管理', path: '/projects', permission: 'supervisor:project:list' },
  { label: '塔吊监测', path: '/tower-monitor', permission: 'supervisor:device:list' },
  { label: '环境监测', path: '/environment', permission: 'supervisor:monitor:list' },
  { label: '视频中心', path: '/video' },
]
const managementMenus = [
  { label: '设备管理', desc: '设备档案与在线状态', path: '/equipment', permission: 'supervisor:device:list', icon: Construction },
  { label: '报警管理', desc: '报警处置与整改闭环', path: '/alarms', permission: 'supervisor:alarm:list', icon: ShieldAlert },
  { label: '摄像头管理', desc: '视频设备档案与定位', path: '/cameras', icon: Camera },
  { label: '基坑管理', desc: '基坑档案与风险监测', path: '/foundation-pits', icon: HardHat },
  { label: '维护管理', desc: '维修保养与到期提醒', path: '/maintenance', icon: Wrench },
  { label: '监测配置', desc: '阈值规则与联动策略', path: '/monitor-config', permission: 'supervisor:alarm-rule:list', icon: ShieldCheck },
]
const visiblePrimaryMenus = computed(() => primaryMenus.filter((item) => hasPermission(item.permission)))
const visibleManagementMenus = computed(() => managementMenus.filter((item) => hasPermission(item.permission)))
const managementActive = computed(() => visibleManagementMenus.value.some((item) => activePath.value === item.path))
const activePath = computed(() => route.path)
const navigate = (path: string) => { mobileOpen.value = false; void router.push(path) }
const logout = () => { clearToken(); void router.replace('/login') }
const submitPassword = async () => {
  if (!passwordForm.value.oldPassword || passwordForm.value.newPassword.length < 8) return ElMessage.warning('请填写旧密码，新密码至少 8 位')
  changingPassword.value = true
  try { await changePassword(passwordForm.value); ElMessage.success('密码修改成功，请重新登录'); passwordDialog.value = false; logout() }
  finally { changingPassword.value = false }
}
</script>

<template>
  <header class="app-header">
    <div class="brand" role="button" tabindex="0" @click="navigate('/')" @keydown.enter="navigate('/')">
      <span class="brand-mark"><Building2 :size="25" /><span class="signal-bars" /></span>
      <span class="brand-copy">
        <strong>智慧城市建设安全监控平台</strong>
        <small>INTELLIGENT CITY SAFETY MONITORING</small>
      </span>
    </div>

    <nav class="main-nav" aria-label="主导航">
      <button v-for="item in visiblePrimaryMenus" :key="item.label" :class="{ active: activePath === item.path }" @click="navigate(item.path)">
        {{ item.label }}
      </button>
      <el-dropdown placement="bottom-end" trigger="click" popper-class="management-popper" @command="navigate">
        <button class="management-trigger" :class="{ active: managementActive }">管理中心 <ChevronDown :size="13" /></button>
        <template #dropdown><el-dropdown-menu class="management-menu"><el-dropdown-item v-for="item in visibleManagementMenus" :key="item.path" :command="item.path" :class="{ selected: activePath===item.path }"><span class="management-icon"><component :is="item.icon" :size="17" /></span><span class="management-copy"><b>{{ item.label }}</b><small>{{ item.desc }}</small></span><span class="menu-arrow">›</span></el-dropdown-item></el-dropdown-menu></template>
      </el-dropdown>
    </nav>

    <div class="header-actions">
      <button class="icon-btn notice" aria-label="消息提醒" @click="navigate('/alarms')"><Bell :size="18" /><span v-if="store.unresolvedAlarmCount" class="notice-count">{{ store.unresolvedAlarmCount }}</span></button>
      <button class="icon-btn" aria-label="修改密码" @click="passwordDialog=true"><Settings :size="18" /></button>
      <button class="user-chip" title="退出登录" @click="logout"><span class="avatar"><UserRound :size="17" /></span><span>监管中心</span><ChevronDown :size="13" /></button>
      <button class="mobile-trigger" aria-label="打开导航" @click="mobileOpen = !mobileOpen"><X v-if="mobileOpen" /><Menu v-else /></button>
    </div>

    <Transition name="page">
      <nav v-if="mobileOpen" class="mobile-nav">
        <button v-for="item in [...visiblePrimaryMenus,...visibleManagementMenus]" :key="item.label" :class="{ active: activePath === item.path }" @click="navigate(item.path)">{{ item.label }}</button>
      </nav>
    </Transition>
    <el-dialog v-model="passwordDialog" title="修改登录密码" width="min(440px,92vw)" append-to-body>
      <el-form label-width="80px"><el-form-item label="旧密码"><el-input v-model="passwordForm.oldPassword" type="password" show-password /></el-form-item><el-form-item label="新密码"><el-input v-model="passwordForm.newPassword" type="password" show-password placeholder="8-64 位" /></el-form-item></el-form>
      <template #footer><button class="tech-button secondary" @click="passwordDialog=false">取消</button><button class="tech-button" :disabled="changingPassword" @click="submitPassword">{{ changingPassword?'提交中...':'修改密码' }}</button></template>
    </el-dialog>
  </header>
</template>

<style scoped>
.app-header { position: fixed; inset: 0 0 auto; z-index: 1000; display: flex; align-items: center; height: 78px; padding: 0 24px; border-bottom: 1px solid rgba(65,156,235,.22); background: linear-gradient(90deg, rgba(5,22,42,.98), rgba(10,42,75,.97) 52%, rgba(5,22,42,.98)); box-shadow: 0 10px 35px rgba(0,8,20,.38); }
.app-header::after { position: absolute; inset: auto 0 -1px; height: 1px; content: ''; background: linear-gradient(90deg, transparent, #1c84e8 35%, #31d2ff 50%, #1c84e8 65%, transparent); opacity: .7; }
.brand { display: flex; align-items: center; gap: 11px; min-width: 315px; cursor: pointer; }
.brand-mark { position: relative; display: grid; place-items: center; width: 43px; height: 43px; color: #7ec8ff; border: 1px solid rgba(73,175,255,.3); background: linear-gradient(135deg, rgba(26,113,190,.25), rgba(14,44,77,.3)); clip-path: polygon(15% 0, 100% 0, 100% 85%, 85% 100%, 0 100%, 0 15%); }
.signal-bars { position: absolute; right: 4px; bottom: 6px; width: 10px; height: 10px; border-right: 1px solid #21d4ff; border-top: 1px solid #21d4ff; }
.brand-copy { display: flex; flex-direction: column; line-height: 1.1; }
.brand-copy strong { color: #f0f7ff; font-size: 16px; letter-spacing: .14em; white-space: nowrap; }
.brand-copy small { margin-top: 6px; color: #6f9fc7; font-size: 8px; letter-spacing: .16em; }
.main-nav { display: flex; align-items: stretch; height: 100%; }
.main-nav :deep(.el-tooltip__trigger) { height: 100%; }
.main-nav button { position: relative; display: flex; align-items: center; gap: 5px; padding: 0 16px; border: 0; color: #a9bfd6; background: transparent; font-size: 13px; cursor: pointer; transition: color .2s, background .2s; }
.main-nav button:hover, .main-nav button.active { color: #f0f7ff; background: linear-gradient(180deg, transparent, rgba(22,119,255,.12)); }
.main-nav button.active::after { position: absolute; right: 14px; bottom: 0; left: 14px; height: 2px; content: ''; background: #27b8ff; box-shadow: 0 0 10px #27b8ff; }
.management-menu { width: 290px; padding: 8px !important; background: linear-gradient(145deg,#0d2c4c,#071b31) !important; }
.management-menu :deep(.el-dropdown-menu__item) { display: grid; grid-template-columns: 36px 1fr 12px; align-items: center; gap: 9px; min-height: 58px; margin-bottom: 4px; padding: 7px 9px; border: 1px solid transparent; border-radius: 6px; color: #8caac3; }
.management-menu :deep(.el-dropdown-menu__item:hover), .management-menu :deep(.el-dropdown-menu__item.selected) { color: #eaf6ff; border-color: rgba(79,168,255,.22); background: rgba(22,119,255,.13); }
.management-icon { display: grid; place-items: center; width: 34px; height: 34px; border-radius: 6px; color: #4db8fc; background: rgba(22,119,255,.13); }
.management-copy { display: grid; gap: 3px; }.management-copy b { color: #d7e8f5; font-size: 11px; }.management-copy small { color: #5f809d; font-size: 8px; }.menu-arrow { color: #4f7c9e; }
.header-actions { display: flex; align-items: center; gap: 8px; margin-left: auto; }
.icon-btn { position: relative; display: grid; place-items: center; width: 34px; height: 34px; padding: 0; border: 1px solid transparent; border-radius: 6px; color: #8eabc6; background: transparent; cursor: pointer; }
.icon-btn:hover { color: #e9f5ff; border-color: rgba(73,175,255,.22); background: rgba(22,119,255,.1); }
.notice-count { position: absolute; top: 0; right: 0; min-width: 15px; height: 15px; padding: 0 3px; border: 2px solid #07172a; border-radius: 8px; color: white; background: #ff5e72; font-size: 8px; line-height: 11px; }
.user-chip { display: flex; align-items: center; gap: 7px; margin-left: 4px; padding: 4px 8px 4px 4px; border: 1px solid rgba(73,175,255,.14); border-radius: 22px; color: #a9bfd6; background: rgba(18,52,91,.4); font-size: 11px; }
.avatar { display: grid; place-items: center; width: 29px; height: 29px; border-radius: 50%; color: #8bd4ff; background: linear-gradient(145deg, #164d7c, #0b2948); }
.mobile-trigger { display: none; color: #a9bfd6; border: 0; background: transparent; }
.mobile-nav { position: absolute; top: 67px; right: 12px; left: 12px; display: grid; grid-template-columns: repeat(3,1fr); gap: 8px; padding: 12px; border: 1px solid rgba(79,168,255,.24); border-radius: 10px; background: rgba(6,24,45,.98); box-shadow: 0 18px 48px rgba(0,0,0,.4); }
.mobile-nav button { padding: 11px 8px; border: 1px solid rgba(79,168,255,.12); border-radius: 6px; color: #9db7ce; background: rgba(18,52,91,.45); }
.mobile-nav button.active { color: white; border-color: #1677ff; background: rgba(22,119,255,.2); }
@media (max-width: 1320px) { .brand { min-width: 270px; } .brand-copy strong { font-size: 14px; } .main-nav button { padding: 0 11px; } }
@media (max-width: 1100px) { .app-header { height: 68px; padding: 0 14px; } .main-nav { display: none; } .mobile-trigger { display: block; } .brand { min-width: 0; } }
@media (max-width: 600px) { .brand-copy strong { font-size: 12px; letter-spacing: .08em; } .brand-copy small, .user-chip span:not(.avatar), .user-chip > svg, .header-actions > .icon-btn:nth-child(2) { display: none; } .brand-mark { width: 37px; height: 37px; } .mobile-nav { grid-template-columns: repeat(2,1fr); } }
</style>
