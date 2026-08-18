<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'
import { Camera, ChevronDown, ChevronRight, Circle, Grid2X2, ListFilter, Maximize, MonitorPlay, Play, Radio, Search, Signal, Video as VideoIcon, Volume2, VolumeX } from 'lucide-vue-next'
import sceneImage from '@/assets/construction-digital-twin.png'
import { getCameraPage, type CameraRecord } from '@/api/camera'

interface CameraItem { id: number; name: string; area: string; status: '在线' | '离线'; code: string }
const cameras = ref<CameraItem[]>([])
const cameraLoading = ref(false)
const cameraError = ref('')
const cameraPage = ref(1)
const cameraPageSize = 20
const cameraTotal = ref(0)
const visibleIds = ref<number[]>([])
const activeSlot = ref(0)
const query = ref('')
const muted = ref(true)
const now = ref(new Date())
let timer: number | undefined
const filteredCameras = computed(() => cameras.value.filter((item) => !query.value || item.name.includes(query.value) || item.code.includes(query.value) || item.area.includes(query.value)))
const visibleCameras = computed(() => visibleIds.value.map((id) => cameras.value.find((item) => item.id === id)!).filter(Boolean))
const onlineCount = computed(() => cameras.value.filter((item) => item.status === '在线').length)
const clock = computed(() => now.value.toLocaleString('zh-CN', { hour12: false }).replaceAll('/', '-'))
const mapCamera = (item: CameraRecord): CameraItem => ({
  id: item.id,
  name: item.cameraName || `摄像头 ${item.id}`,
  area: item.installationAddress || item.cameraType || '未配置安装位置',
  status: item.status === 1 ? '在线' : '离线',
  code: item.cameraCode || `CAM-${item.id}`,
})
async function loadCameras() {
  cameraLoading.value = true
  cameraError.value = ''
  try {
    const response = await getCameraPage(cameraPage.value, cameraPageSize)
    cameras.value = response.data.records.map(mapCamera)
    cameraTotal.value = response.data.total
    const online = cameras.value.filter((item) => item.status === '在线')
    const preferred = [...online, ...cameras.value.filter((item) => item.status !== '在线')].slice(0, 4)
    if (!visibleIds.value.length || !visibleIds.value.some((id) => cameras.value.some((item) => item.id === id))) visibleIds.value = preferred.map((item) => item.id)
    activeSlot.value = Math.min(activeSlot.value, Math.max(visibleIds.value.length - 1, 0))
  } catch (error) {
    cameraError.value = error instanceof Error ? error.message : '摄像头列表加载失败'
  } finally {
    cameraLoading.value = false
  }
}
function selectCamera(item: CameraItem) { if (item.status === '离线') return; visibleIds.value[activeSlot.value] = item.id; visibleIds.value = [...visibleIds.value] }
async function fullscreen(index: number) { const panel = document.querySelectorAll<HTMLElement>('.video-tile')[index]; if (!document.fullscreenElement) await panel?.requestFullscreen(); else await document.exitFullscreen() }
onMounted(() => { void loadCameras(); timer = window.setInterval(() => { now.value = new Date() }, 1000) })
onBeforeUnmount(() => window.clearInterval(timer))
</script>

<template>
  <div class="view-page video-page">
    <div class="page-head">
      <div><p class="page-eyebrow">AI VIDEO SURVEILLANCE CENTER</p><h1 class="page-title">施工现场视频监控</h1><p class="page-subtitle">多路高清视频融合与 AI 安全行为识别</p></div>
      <div class="video-summary"><span><i class="live-dot" />当前页在线 <b>{{ onlineCount }}</b></span><span>摄像头总数 <b>{{ cameraTotal }}</b></span><span>AI 识别事件 <b>7</b></span></div>
    </div>

    <div class="video-layout">
      <aside class="tech-panel camera-sidebar">
        <div class="panel-head"><h2 class="panel-title">摄像头列表</h2><span class="panel-meta">{{ onlineCount }} / {{ cameraTotal }} 在线</span></div>
        <label class="camera-search"><Search :size="14" /><input v-model="query" placeholder="搜索摄像头或区域" /><ListFilter :size="13" /></label>
        <div class="camera-group-title"><span><ChevronDown :size="13" />数据库摄像头设备</span><b>{{ cameraTotal }}</b></div>
        <div class="camera-list">
          <div v-if="cameraLoading" class="camera-list-state"><span class="camera-loader" />正在加载摄像头...</div>
          <div v-else-if="cameraError" class="camera-list-state error"><span>{{ cameraError }}</span><button @click="loadCameras">重新加载</button></div>
          <div v-else-if="!filteredCameras.length" class="camera-list-state">暂无摄像头设备</div>
          <template v-else>
            <button v-for="item in filteredCameras" :key="item.id" :class="{ active: visibleIds[activeSlot] === item.id, offline: item.status === '离线' }" @click="selectCamera(item)">
              <span class="camera-icon"><Camera :size="16" /></span><span class="camera-copy"><b>{{ item.name }}</b><small>{{ item.area }} · {{ item.code }}</small></span><span class="camera-state"><i />{{ item.status }}</span><ChevronRight :size="13" />
            </button>
          </template>
        </div>
        <div v-if="cameraTotal > cameraPageSize" class="camera-pagination"><el-pagination v-model:current-page="cameraPage" small layout="prev, next" :page-size="cameraPageSize" :total="cameraTotal" @current-change="loadCameras" /></div>
        <div class="sidebar-foot"><Signal :size="13" /><span>数据来源 /supervisor/camera/page</span><b>{{ cameraLoading ? '同步中' : '已同步' }}</b></div>
      </aside>

      <section class="tech-panel video-wall">
        <div class="wall-toolbar">
          <div><span class="site-badge"><MonitorPlay :size="14" /> 实时监控</span><b>4 路画面</b></div>
          <div><button class="active" aria-label="四宫格"><Grid2X2 :size="15" /></button><button @click="muted = !muted" :aria-label="muted ? '开启声音' : '关闭声音'"><VolumeX v-if="muted" :size="15" /><Volume2 v-else :size="15" /></button><span>{{ clock }}</span></div>
        </div>
        <div class="video-grid">
          <article v-for="(camera, index) in visibleCameras" :key="`${camera.id}-${index}`" class="video-tile" :class="{ selected: activeSlot === index }" @click="activeSlot = index">
            <video :poster="sceneImage" preload="metadata" playsinline :muted="muted" />
            <div class="camera-treatment" :class="`view-${index + 1}`" />
            <div class="video-scanline" />
            <div class="video-topbar"><span><i class="record-dot" /> REC</span><b>CH {{ String(index + 1).padStart(2, '0') }}</b><button aria-label="全屏" @click.stop="fullscreen(index)"><Maximize :size="14" /></button></div>
            <div class="video-ai-box" :class="{ person: index === 0, vehicle: index === 2 }"><span>{{ index === 0 ? '人员 03' : index === 2 ? '车辆 01' : '区域正常' }}</span></div>
            <div class="video-bottombar"><span><Camera :size="13" /><b>{{ camera.name }}</b><small>{{ camera.code }}</small></span><span>{{ clock.split(' ')[1] }}</span></div>
            <button class="play-button" aria-label="播放模拟视频"><Play :size="18" fill="currentColor" /></button>
          </article>
        </div>
        <div class="wall-footer"><span><Radio :size="13" />视频流状态 <b>稳定</b></span><span>平均码率 <b>4.2 Mbps</b></span><span>画面延迟 <b>32 ms</b></span><span>AI 识别 <b>已开启</b></span></div>
      </section>

      <aside class="video-side-info">
        <article class="tech-panel ai-events"><div class="panel-head"><h2 class="panel-title">AI 识别事件</h2><span class="panel-meta">今日 7 起</span></div><div class="ai-event urgent"><span class="event-thumb"><HardHatIcon /></span><div><b>未佩戴安全帽</b><small>东区塔吊作业面 · 10:21</small></div><span>待处理</span></div><div class="ai-event"><span class="event-thumb"><Circle :size="17" /></span><div><b>人员进入警戒区域</b><small>深基坑北侧 · 09:48</small></div><span>已确认</span></div><div class="ai-event"><span class="event-thumb"><VideoIcon :size="17" /></span><div><b>通道长时间占用</b><small>主出入口通道 · 08:36</small></div><span>已完成</span></div></article>
        <article class="tech-panel active-camera"><div class="panel-head"><h2 class="panel-title">当前通道信息</h2></div><dl><div><dt>摄像头名称</dt><dd>{{ visibleCameras[activeSlot]?.name }}</dd></div><div><dt>设备编号</dt><dd>{{ visibleCameras[activeSlot]?.code }}</dd></div><div><dt>所属区域</dt><dd>{{ visibleCameras[activeSlot]?.area }}</dd></div><div><dt>视频质量</dt><dd>1080P / 25FPS</dd></div><div><dt>AI 算法</dt><dd class="green">安全帽 · 入侵 · 烟火</dd></div></dl></article>
      </aside>
    </div>
  </div>
</template>

<script lang="ts">
import { HardHat as HardHatIcon } from 'lucide-vue-next'
export default { components: { HardHatIcon } }
</script>

<style scoped>
.video-summary { display: flex; align-items: center; height: 42px; border: 1px solid rgba(79,168,255,.14); border-radius: 6px; background: rgba(10,35,62,.45); }.video-summary span { display: flex; align-items: center; gap: 6px; padding: 0 13px; border-right: 1px solid rgba(79,168,255,.12); color: #6e8da9; font-size: 8px; }.video-summary span:last-child { border: 0; }.video-summary b { color: #cde1f3; font-size: 11px; }.video-layout { display: grid; grid-template-columns: 280px minmax(610px,1fr) 290px; gap: 12px; min-height: calc(100vh - 174px); }.camera-sidebar { display: flex; flex-direction: column; }.camera-search { display: flex; align-items: center; gap: 7px; height: 36px; margin: 12px 11px 8px; padding: 0 9px; border: 1px solid rgba(79,168,255,.13); border-radius: 5px; color: #577694; background: rgba(10,35,62,.5); }.camera-search input { width: 100%; border: 0; outline: 0; color: #b6cada; background: transparent; font-size: 9px; }.camera-group-title { display: flex; align-items: center; justify-content: space-between; padding: 8px 12px; color: #8aa5bc; font-size: 9px; }.camera-group-title span { display: flex; align-items: center; gap: 5px; }.camera-group-title b { display: grid; place-items: center; min-width: 20px; height: 18px; border-radius: 10px; color: #6490b3; background: rgba(79,168,255,.08); }.camera-list { flex: 1; padding: 0 8px; overflow-y: auto; }.camera-list button { display: grid; grid-template-columns: 34px 1fr auto 12px; align-items: center; gap: 7px; width: 100%; min-height: 58px; margin-bottom: 5px; padding: 6px 8px; border: 1px solid transparent; border-radius: 5px; color: #617f9b; background: rgba(18,52,91,.2); cursor: pointer; text-align: left; }.camera-list button:hover, .camera-list button.active { border-color: rgba(79,168,255,.24); background: rgba(22,119,255,.1); }.camera-list button.offline { opacity: .45; cursor: not-allowed; }.camera-icon { display: grid; place-items: center; width: 32px; height: 32px; border-radius: 5px; color: #49b8ff; background: rgba(22,119,255,.11); }.camera-copy { display: flex; min-width: 0; flex-direction: column; }.camera-copy b { overflow: hidden; color: #cbdce9; font-size: 9px; font-weight: 500; text-overflow: ellipsis; white-space: nowrap; }.camera-copy small { margin-top: 4px; color: #52718e; font-size: 7px; }.camera-state { display: flex; align-items: center; gap: 3px; color: #36d399; font-size: 7px; }.camera-state i { width: 5px; height: 5px; border-radius: 50%; background: currentColor; }.offline .camera-state { color: #8a9baa; }.sidebar-foot { display: flex; align-items: center; gap: 6px; min-height: 40px; padding: 0 12px; border-top: 1px solid rgba(79,168,255,.1); color: #587895; font-size: 8px; }.sidebar-foot b { margin-left: auto; color: #36d399; }
.video-wall { display: flex; min-height: 700px; flex-direction: column; }.wall-toolbar { display: flex; align-items: center; justify-content: space-between; min-height: 50px; padding: 0 12px; border-bottom: 1px solid rgba(79,168,255,.12); }.wall-toolbar > div { display: flex; align-items: center; gap: 8px; }.site-badge { display: flex; align-items: center; gap: 6px; padding: 5px 8px; border-radius: 4px; color: #53c5ff; background: rgba(22,119,255,.1); font-size: 9px; }.wall-toolbar b { color: #a7bfd3; font-size: 9px; }.wall-toolbar button { display: grid; place-items: center; width: 29px; height: 29px; border: 1px solid rgba(79,168,255,.13); border-radius: 4px; color: #7594b0; background: rgba(18,52,91,.35); cursor: pointer; }.wall-toolbar button:hover, .wall-toolbar button.active { color: #dcedfa; border-color: #1677ff; background: rgba(22,119,255,.2); }.wall-toolbar > div:last-child > span { margin-left: 5px; color: #6c8aa6; font-family: monospace; font-size: 9px; }.video-grid { display: grid; grid-template-columns: repeat(2,1fr); flex: 1; gap: 6px; padding: 7px; background: #030b14; }.video-tile { position: relative; min-height: 280px; overflow: hidden; border: 1px solid rgba(79,168,255,.17); border-radius: 4px; background: #06101a; cursor: pointer; }.video-tile.selected { border-color: #289dff; box-shadow: 0 0 0 1px rgba(40,157,255,.4), inset 0 0 35px rgba(22,119,255,.16); }.video-tile video { width: 100%; height: 100%; object-fit: cover; filter: saturate(.55) contrast(1.06) brightness(.52); }.camera-treatment { position: absolute; inset: 0; pointer-events: none; background: linear-gradient(180deg,rgba(1,12,23,.2),transparent 45%,rgba(1,12,23,.62)), linear-gradient(90deg,rgba(7,56,91,.22),transparent 30%); }.camera-treatment.view-2 { backdrop-filter: hue-rotate(20deg); }.camera-treatment.view-3 { backdrop-filter: grayscale(.15) brightness(.82); }.camera-treatment.view-4 { background: linear-gradient(150deg,rgba(27,78,114,.23),transparent 40%,rgba(3,13,24,.7)); }.video-scanline { position: absolute; top: 0; width: 100%; height: 2px; background: rgba(61,203,255,.38); box-shadow: 0 0 8px rgba(61,203,255,.5); animation: scan 7s linear infinite; }.video-topbar, .video-bottombar { position: absolute; right: 0; left: 0; display: flex; align-items: center; justify-content: space-between; padding: 8px 9px; color: #b7cbda; font-size: 8px; }.video-topbar { top: 0; background: linear-gradient(180deg,rgba(1,10,19,.82),transparent); }.video-topbar span { display: flex; align-items: center; gap: 5px; color: #ff6a79; font-weight: 700; }.record-dot { width: 6px; height: 6px; border-radius: 50%; background: currentColor; animation: pulse-dot 1.4s infinite; }.video-topbar b { margin-left: auto; margin-right: 8px; color: #6f8ca5; }.video-topbar button { display: grid; place-items: center; padding: 3px; border: 0; color: #9ab6ca; background: transparent; cursor: pointer; }.video-bottombar { bottom: 0; align-items: end; background: linear-gradient(0deg,rgba(1,10,19,.9),transparent); }.video-bottombar > span:first-child { display: grid; grid-template-columns: 16px 1fr; align-items: center; }.video-bottombar b { color: #dbe8f2; font-size: 9px; }.video-bottombar small { grid-column: 2; margin-top: 2px; color: #6988a0; font-size: 7px; }.video-ai-box { position: absolute; top: 34%; left: 32%; width: 30%; height: 38%; border: 1px solid rgba(54,211,153,.6); opacity: .55; }.video-ai-box::before, .video-ai-box::after { position: absolute; width: 9px; height: 9px; content: ''; }.video-ai-box::before { top: -1px; left: -1px; border-top: 2px solid #36d399; border-left: 2px solid #36d399; }.video-ai-box::after { right: -1px; bottom: -1px; border-right: 2px solid #36d399; border-bottom: 2px solid #36d399; }.video-ai-box span { position: absolute; top: -16px; left: -1px; padding: 2px 4px; color: #36d399; background: rgba(4,30,39,.75); font-size: 7px; white-space: nowrap; }.video-ai-box:not(.person):not(.vehicle) { top: 46%; left: 43%; width: 15%; height: 18%; }.play-button { position: absolute; top: 50%; left: 50%; display: grid; place-items: center; width: 38px; height: 38px; border: 1px solid rgba(158,211,245,.35); border-radius: 50%; color: rgba(218,239,252,.75); background: rgba(5,25,43,.58); opacity: 0; cursor: pointer; transform: translate(-50%,-50%); transition: opacity .2s; }.video-tile:hover .play-button { opacity: 1; }.wall-footer { display: grid; grid-template-columns: repeat(4,1fr); min-height: 42px; border-top: 1px solid rgba(79,168,255,.11); }.wall-footer span { display: flex; align-items: center; justify-content: center; gap: 5px; border-right: 1px solid rgba(79,168,255,.09); color: #5d7c98; font-size: 8px; }.wall-footer b { color: #9fc1d9; }
.video-side-info { display: grid; align-content: start; gap: 12px; }.ai-event { display: grid; grid-template-columns: 42px 1fr auto; align-items: center; gap: 7px; min-height: 67px; margin: 9px 10px; padding: 7px; border: 1px solid rgba(79,168,255,.1); border-radius: 5px; background: rgba(18,52,91,.22); }.ai-event.urgent { border-color: rgba(255,94,114,.18); background: rgba(255,94,114,.05); }.event-thumb { display: grid; place-items: center; width: 40px; height: 40px; border-radius: 5px; color: #47b7ff; background: linear-gradient(135deg,#123c62,#0a223a); }.urgent .event-thumb { color: #ff7181; }.ai-event div { display: flex; min-width: 0; flex-direction: column; }.ai-event b { overflow: hidden; color: #c9d9e6; font-size: 9px; font-weight: 500; text-overflow: ellipsis; white-space: nowrap; }.ai-event small { margin-top: 5px; color: #587793; font-size: 7px; }.ai-event > span:last-child { color: #6e8da8; font-size: 7px; }.urgent > span:last-child { color: #ff7181; }.active-camera dl { margin: 0; padding: 5px 12px 12px; }.active-camera dl div { display: flex; align-items: center; justify-content: space-between; min-height: 40px; border-bottom: 1px solid rgba(79,168,255,.09); }.active-camera dt { color: #5d7c98; font-size: 8px; }.active-camera dd { margin: 0; color: #a8bfd2; font-size: 8px; text-align: right; }.active-camera dd.green { color: #36d399; }
@media (max-width: 1200px) { .video-layout { grid-template-columns: 240px 1fr; }.video-side-info { grid-column: 1 / -1; grid-template-columns: repeat(2,1fr); } }
@media (max-width: 800px) { .video-layout { grid-template-columns: 1fr; }.camera-sidebar { max-height: 380px; }.video-side-info { grid-template-columns: 1fr; }.video-grid { grid-template-columns: 1fr; }.video-tile { min-height: 330px; }.video-summary { display: none; } }
@media (max-width: 450px) { .video-tile { min-height: 240px; }.wall-footer { grid-template-columns: repeat(2,1fr); }.wall-footer span { min-height: 34px; }.wall-toolbar > div:last-child > span { display: none; } }
</style>

<style scoped>
.camera-list-state { display: flex; align-items: center; justify-content: center; flex-direction: column; gap: 8px; min-height: 170px; padding: 16px; color: #6686a2; font-size: 9px; text-align: center; }
.camera-list-state.error { color: #ff7181; }
.camera-list-state button { min-height: 28px; margin-top: 3px; padding: 0 10px; border: 1px solid rgba(79,168,255,.25); border-radius: 4px; color: #81c9f7; background: rgba(22,119,255,.12); cursor: pointer; }
.camera-loader { width: 22px; height: 22px; border: 2px solid rgba(79,168,255,.15); border-top-color: #38bdf8; border-radius: 50%; animation: camera-spin .8s linear infinite; }
.camera-pagination { display: flex; align-items: center; justify-content: center; min-height: 38px; border-top: 1px solid rgba(79,168,255,.08); }
@keyframes camera-spin { to { transform: rotate(360deg); } }
</style>
