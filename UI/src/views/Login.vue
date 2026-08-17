<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Building2, LockKeyhole, UserRound } from 'lucide-vue-next'
import { login } from '@/api'
import { setToken } from '@/api/http'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const form = reactive({ username: 'supervisor', password: '' })

async function submit() {
  if (!form.username || !form.password) return ElMessage.warning('请输入用户名和密码')
  loading.value = true
  try {
    const response = await login(form.username, form.password)
    setToken(response.data.token)
    ElMessage.success('登录成功')
    const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : '/'
    await router.replace(redirect)
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <main class="login-page">
    <section class="login-card">
      <div class="login-brand"><span><Building2 :size="31" /></span><div><h1>智慧城市建设安全监控平台</h1><p>INTELLIGENT CITY SAFETY MONITORING</p></div></div>
      <div class="login-copy"><small>SUPERVISOR TERMINAL</small><h2>监管端登录</h2><p>登录后查看实时监测、设备运行和告警处置情况</p></div>
      <el-form @submit.prevent="submit">
        <label><span><UserRound :size="17" /></span><input v-model.trim="form.username" autocomplete="username" placeholder="用户名" /></label>
        <label><span><LockKeyhole :size="17" /></span><input v-model="form.password" type="password" autocomplete="current-password" placeholder="密码" @keyup.enter="submit" /></label>
        <button type="submit" :disabled="loading">{{ loading ? '正在登录…' : '登录系统' }}</button>
      </el-form>
      <p class="login-tip">账号由系统管理员统一分配</p>
    </section>
  </main>
</template>

<style scoped>
.login-page { display: grid; min-height: 100vh; place-items: center; padding: 24px; background: radial-gradient(circle at 50% 18%,rgba(22,119,255,.24),transparent 34%),linear-gradient(135deg,#04101f,#082744 52%,#04101f); }
.login-page::before { position: fixed; inset: 0; content: ''; pointer-events: none; background: linear-gradient(rgba(60,154,230,.045) 1px,transparent 1px),linear-gradient(90deg,rgba(60,154,230,.045) 1px,transparent 1px); background-size: 34px 34px; }
.login-card { position: relative; width: min(440px,100%); padding: 32px; border: 1px solid rgba(79,168,255,.28); border-radius: 14px; background: rgba(7,29,53,.92); box-shadow: 0 28px 90px rgba(0,6,17,.56),inset 0 1px rgba(255,255,255,.04); }
.login-brand { display: flex; align-items: center; gap: 12px; padding-bottom: 24px; border-bottom: 1px solid rgba(79,168,255,.14); }.login-brand>span { display: grid; width: 52px; height: 52px; place-items: center; color:#78c9ff; border:1px solid rgba(79,168,255,.3); background:rgba(22,119,255,.13); }.login-brand h1 { margin:0; color:#eff8ff; font-size:17px; letter-spacing:.08em; }.login-brand p { margin:7px 0 0; color:#6087aa; font-size:8px; letter-spacing:.14em; }
.login-copy { padding:28px 0 20px; }.login-copy small { color:#42bdff; font-size:10px; letter-spacing:.18em; }.login-copy h2 { margin:7px 0; color:#e9f4ff; font-size:24px; }.login-copy p,.login-tip { margin:0; color:#7695b1; font-size:11px; }
.login-card form { display:grid; gap:13px; }.login-card label { display:flex; align-items:center; height:46px; padding:0 13px; border:1px solid rgba(79,168,255,.17); border-radius:6px; color:#6698c1; background:rgba(9,39,70,.62); }.login-card input { width:100%; height:100%; margin-left:10px; border:0; outline:0; color:#e7f2ff; background:transparent; }.login-card input::placeholder { color:#557795; }.login-card button { height:45px; margin-top:5px; border:1px solid #299eff; border-radius:6px; color:white; background:linear-gradient(90deg,#1269dd,#169cf4); cursor:pointer; box-shadow:0 9px 24px rgba(22,119,255,.22); }.login-card button:disabled { opacity:.65; cursor:wait; }.login-tip { margin-top:18px; text-align:center; }
</style>
