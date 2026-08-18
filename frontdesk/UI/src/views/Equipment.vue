<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Box, Building2, ChevronRight, Construction, Crosshair, HardHat, Maximize, Minus, Plus, RotateCcw, ScanLine, Search, Signal, TowerControl, Wifi } from 'lucide-vue-next'
import Sidebar from '@/layout/Sidebar.vue'
import { useMonitorStore } from '@/store/monitor'
import type { DeviceRecord } from '@/api'
import sceneImage from '@/assets/construction-digital-twin.png'

const store = useMonitorStore()
const activeType = ref('全部')
const selected = ref<DeviceRecord | null>(null)
const query = ref('')
const scale = ref(1)
const rotation = ref(0)
const offset = ref({ x: 0, y: 0 })
const dragging = ref(false)
const dragStart = ref({ x: 0, y: 0 })
const stage = ref<HTMLElement>()

const categories = [
  { label: '设备总览', key: '全部', icon: Box },
  { label: '塔吊', key: '塔吊', icon: Construction },
  { label: '升降机', key: '升降机', icon: Building2 },
  { label: '深基坑', key: '深基坑', icon: HardHat },
  { label: '高支模', key: '高支模', icon: TowerControl },
]
const filtered = computed(() => store.devices.filter((item) => (activeType.value === '全部' || item.type === activeType.value) && (!query.value || item.name.includes(query.value) || item.code.toLowerCase().includes(query.value.toLowerCase()))))
const markers = [
  { x: 72, y: 26, id: 1 }, { x: 52, y: 70, id: 2 }, { x: 38, y: 48, id: 3 }, { x: 83, y: 57, id: 4 }, { x: 21, y: 63, id: 5 }, { x: 61, y: 41, id: 6 },
]
const statusClass = (status: string) => status === '运行' ? 'status-running' : status === '报警' ? 'status-alarm' : 'status-offline'
const resetView = () => { scale.value = 1; rotation.value = 0; offset.value = { x: 0, y: 0 } }
const zoom = (delta: number) => { scale.value = Math.min(1.6, Math.max(.85, scale.value + delta)) }
function startDrag(event: PointerEvent) { dragging.value = true; dragStart.value = { x: event.clientX - offset.value.x, y: event.clientY - offset.value.y }; stage.value?.setPointerCapture(event.pointerId) }
function moveDrag(event: PointerEvent) { if (!dragging.value) return; offset.value = { x: event.clientX - dragStart.value.x, y: event.clientY - dragStart.value.y } }
function endDrag() { dragging.value = false }
function openDevice(device?: DeviceRecord) { if (!device) return; selected.value = device }
async function fullscreen() { if (!document.fullscreenElement) await stage.value?.requestFullscreen(); else await document.exitFullscreen() }
onMounted(async () => { if (!store.devices.length) await store.fetchDashboard() })
</script>

<template>
  <div class="view-page equipment-page">
    <div class="page-head">
      <div><p class="page-eyebrow">CONSTRUCTION DIGITAL TWIN</p><h1 class="page-title">施工设备数字孪生监控</h1><p class="page-subtitle">三维场景与现场物联设备状态实时映射</p></div>
      <div class="sync-status"><i class="live-dot" /> 设备数据在线 · {{ store.devices.filter(d => d.status === '运行').length }} 台运行中</div>
    </div>

    <div class="equipment-layout">
      <Sidebar :items="categories" :active="activeType" @change="activeType = $event" />

      <section class="tech-panel twin-panel">
        <div class="twin-toolbar">
          <div><span class="site-badge"><Crosshair :size="13" /> A-01 中心城区项目</span><b>施工总平面实时模型</b></div>
          <div class="model-status"><span><i class="live-dot" />模型在线</span><span>LOD 2.5</span><span>数据延迟 0.8s</span></div>
        </div>
        <div ref="stage" class="model-stage" :class="{ dragging }" @pointerdown="startDrag" @pointermove="moveDrag" @pointerup="endDrag" @pointercancel="endDrag" @wheel.prevent="zoom($event.deltaY > 0 ? -.08 : .08)">
          <div class="model-image" :style="{ transform: `translate(${offset.x}px, ${offset.y}px) scale(${scale}) rotate(${rotation}deg)` }"><img :src="sceneImage" alt="施工现场三维数字孪生场景" /></div>
          <div class="model-grid" />
          <button v-for="marker in markers" :key="marker.id" class="model-marker" :class="statusClass(store.devices.find(d => d.id === marker.id)?.status ?? '运行')" :style="{ left: `${marker.x}%`, top: `${marker.y}%` }" @pointerdown.stop @click="openDevice(store.devices.find(d => d.id === marker.id))">
            <span><component :is="store.devices.find(d => d.id === marker.id)?.type === '塔吊' ? Construction : Signal" :size="15" /></span>
            <b>{{ store.devices.find(d => d.id === marker.id)?.name }}</b>
          </button>
          <div class="model-coordinate"><b>N</b><span>31°14'29.4" N</span><span>121°29'36.8" E</span></div>
          <div class="model-controls">
            <button aria-label="放大" @pointerdown.stop @click="zoom(.1)"><Plus :size="16" /></button>
            <button aria-label="缩小" @pointerdown.stop @click="zoom(-.1)"><Minus :size="16" /></button>
            <button aria-label="旋转" @pointerdown.stop @click="rotation += 5"><RotateCcw :size="16" /></button>
            <button aria-label="重置视角" @pointerdown.stop @click="resetView"><Crosshair :size="16" /></button>
            <button aria-label="全屏" @pointerdown.stop @click="fullscreen"><Maximize :size="16" /></button>
          </div>
          <div class="model-hint">拖拽平移 · 滚轮缩放 · 点击设备查看详情</div>
        </div>
        <div class="twin-footer"><span><ScanLine :size="13" /> 模型同步率 <b>99.2%</b></span><span>可视设备 <b>{{ store.devices.length }}</b></span><span>传感点位 <b>128</b></span><span>今日事件 <b>29</b></span></div>
      </section>

      <aside class="tech-panel device-list-panel">
        <div class="panel-head"><h2 class="panel-title">设备实时运行列表</h2><span class="panel-meta">{{ filtered.length }} 项</span></div>
        <label class="device-search"><Search :size="14" /><input v-model="query" placeholder="搜索名称或设备编号" /></label>
        <div class="device-overview"><div><small>设备总数</small><b>{{ store.devices.length }}</b></div><div><small>在线运行</small><b class="green">{{ store.devices.filter(d => d.status === '运行').length }}</b></div><div><small>当前报警</small><b class="red">{{ store.devices.filter(d => d.status === '报警').length }}</b></div></div>
        <div class="device-list">
          <button v-for="device in filtered" :key="device.id" :class="{ selected: selected?.id === device.id }" @click="selected = device">
            <span class="device-list-icon"><component :is="device.type === '塔吊' ? Construction : device.type === '深基坑' ? HardHat : Building2" :size="18" /></span>
            <span class="device-list-copy"><b>{{ device.name }}</b><small>{{ device.code }} · {{ device.updatedAt }}</small></span>
            <span class="signal-bars-ui"><i :style="{ height: '35%' }" /><i :style="{ height: '58%' }" /><i :style="{ height: device.signal > 0 ? '82%' : '15%' }" /></span>
            <span class="status-pill" :class="statusClass(device.status)">{{ device.status }}</span>
            <ChevronRight :size="14" />
          </button>
          <div v-if="!filtered.length" class="empty-state">未找到匹配设备</div>
        </div>
        <div class="list-footer"><Wifi :size="13" /> 物联网关连接正常 <span>6 / 6</span></div>
      </aside>
    </div>

    <el-dialog :model-value="Boolean(selected)" width="450px" :title="selected?.name" destroy-on-close @update:model-value="!$event && (selected = null)">
      <div v-if="selected" class="detail-dialog">
        <div class="detail-id"><span><component :is="selected.type === '塔吊' ? Construction : Building2" :size="24" /></span><div><small>设备唯一标识</small><b>{{ selected.code }}</b></div><span class="status-pill" :class="statusClass(selected.status)">{{ selected.status }}</span></div>
        <div class="detail-grid"><div><small>信号质量</small><b>{{ selected.signal }}%</b></div><div><small>最近同步</small><b>{{ selected.updatedAt }}</b></div><div><small>今日运行</small><b>8.6 h</b></div><div><small>当前告警</small><b>{{ selected.status === '报警' ? 1 : 0 }}</b></div></div>
        <button class="primary-action" @click="selected.type === '塔吊' ? $router.push('/tower-monitor') : ElMessage.success('设备数据已同步')">进入专项监测 <ChevronRight :size="15" /></button>
      </div>
    </el-dialog>
  </div>
</template>

<style scoped>
.equipment-layout { display: grid; grid-template-columns: 150px minmax(520px,1fr) 340px; gap: 12px; min-height: calc(100vh - 174px); }.side-rail { height: fit-content; }.twin-panel { display: flex; min-height: 690px; flex-direction: column; }.twin-toolbar { display: flex; align-items: center; justify-content: space-between; min-height: 58px; padding: 0 15px; border-bottom: 1px solid rgba(79,168,255,.14); }.twin-toolbar > div:first-child { display: flex; align-items: center; gap: 12px; }.twin-toolbar b { color: #dbeaf7; font-size: 13px; }.site-badge { display: flex; align-items: center; gap: 5px; padding: 5px 8px; border: 1px solid rgba(38,184,255,.27); border-radius: 4px; color: #53c5ff; background: rgba(22,119,255,.1); font-size: 9px; }.model-status { display: flex; gap: 14px; color: #6383a0; font-size: 9px; }.model-status span { display: flex; align-items: center; gap: 6px; }
.model-stage { position: relative; flex: 1; min-height: 585px; overflow: hidden; background: #071a30; cursor: grab; touch-action: none; }.model-stage.dragging { cursor: grabbing; }.model-image { position: absolute; inset: -4%; transform-origin: center; transition: transform .18s ease-out; }.dragging .model-image { transition: none; }.model-image img { width: 100%; height: 100%; object-fit: cover; filter: saturate(.83) brightness(.88); user-select: none; pointer-events: none; }.model-grid { position: absolute; inset: 0; pointer-events: none; background: linear-gradient(rgba(38,160,231,.045) 1px, transparent 1px), linear-gradient(90deg, rgba(38,160,231,.045) 1px, transparent 1px); background-size: 55px 55px; box-shadow: inset 0 0 100px rgba(3,15,29,.75); }.model-marker { position: absolute; z-index: 5; display: flex; align-items: center; padding: 0; border: 0; color: #42d9a4; background: transparent; cursor: pointer; transform: translate(-50%,-50%); }.model-marker > span { display: grid; place-items: center; width: 32px; height: 32px; border: 1px solid currentColor; border-radius: 50%; background: rgba(5,26,48,.88); box-shadow: 0 0 0 5px rgba(54,211,153,.11), 0 0 20px currentColor; }.model-marker > b { margin-left: 5px; padding: 5px 8px; border-left: 2px solid currentColor; color: #dcefff; background: rgba(5,26,48,.82); font-size: 9px; font-weight: 500; white-space: nowrap; }.model-marker.status-alarm { color: #ff5e72; }.model-marker.status-offline { color: #8a9baa; }.model-controls { position: absolute; z-index: 6; top: 16px; right: 16px; display: grid; gap: 5px; padding: 5px; border: 1px solid rgba(79,168,255,.2); border-radius: 6px; background: rgba(5,25,46,.8); backdrop-filter: blur(6px); }.model-controls button { display: grid; place-items: center; width: 31px; height: 31px; border: 0; border-radius: 4px; color: #8db6d6; background: rgba(22,78,125,.38); cursor: pointer; }.model-controls button:hover { color: white; background: #1677ff; }.model-coordinate { position: absolute; z-index: 5; bottom: 15px; left: 15px; display: grid; grid-template-columns: 22px 1fr; gap: 2px 8px; padding: 8px 10px; border: 1px solid rgba(79,168,255,.18); border-radius: 5px; color: #7696b3; background: rgba(5,25,46,.74); font-size: 8px; }.model-coordinate b { grid-row: 1 / 3; display: grid; place-items: center; color: #58caff; font-size: 15px; }.model-hint { position: absolute; right: 16px; bottom: 15px; padding: 6px 9px; border-radius: 4px; color: #6d8ba7; background: rgba(5,25,46,.68); font-size: 8px; }.twin-footer { display: grid; grid-template-columns: 1.7fr repeat(3,1fr); min-height: 45px; border-top: 1px solid rgba(79,168,255,.14); }.twin-footer span { display: flex; align-items: center; justify-content: center; gap: 6px; border-right: 1px solid rgba(79,168,255,.1); color: #6787a4; font-size: 9px; }.twin-footer b { color: #cde8ff; }
.device-list-panel { display: flex; min-height: 690px; flex-direction: column; }.device-search { display: flex; align-items: center; gap: 8px; height: 36px; margin: 12px; padding: 0 10px; border: 1px solid rgba(79,168,255,.13); border-radius: 5px; color: #577694; background: rgba(10,35,62,.5); }.device-search input { width: 100%; border: 0; outline: 0; color: #bbcee0; background: transparent; font-size: 10px; }.device-search input::placeholder { color: #4c6d8a; }.device-overview { display: grid; grid-template-columns: repeat(3,1fr); margin: 0 12px 10px; padding: 10px 0; border: 1px solid rgba(79,168,255,.1); border-radius: 5px; background: rgba(18,52,91,.22); }.device-overview div { display: flex; align-items: center; flex-direction: column; border-right: 1px solid rgba(79,168,255,.1); }.device-overview div:last-child { border: 0; }.device-overview small { color: #5e7e9a; font-size: 8px; }.device-overview b { margin-top: 4px; color: #dcebf8; font-size: 17px; }.device-overview .green { color: #42d9a4; }.device-overview .red { color: #ff6679; }.device-list { flex: 1; padding: 0 9px; overflow-y: auto; }.device-list > button { display: grid; grid-template-columns: 36px 1fr 20px 49px 14px; align-items: center; gap: 6px; width: 100%; min-height: 65px; margin-bottom: 6px; padding: 7px; border: 1px solid transparent; border-radius: 6px; color: #718da7; background: rgba(19,55,91,.23); cursor: pointer; text-align: left; }.device-list > button:hover, .device-list > button.selected { border-color: rgba(79,168,255,.24); background: rgba(22,119,255,.1); }.device-list-icon { display: grid; place-items: center; width: 34px; height: 34px; border-radius: 5px; color: #52b9ff; background: rgba(22,119,255,.12); }.device-list-copy { display: flex; min-width: 0; flex-direction: column; }.device-list-copy b { overflow: hidden; color: #d8e6f2; font-size: 10px; font-weight: 500; text-overflow: ellipsis; white-space: nowrap; }.device-list-copy small { margin-top: 5px; color: #577694; font-size: 8px; }.signal-bars-ui { display: flex; align-items: flex-end; gap: 2px; height: 17px; }.signal-bars-ui i { width: 3px; border-radius: 2px; background: #32d5a0; }.list-footer { display: flex; align-items: center; gap: 6px; min-height: 41px; padding: 0 13px; border-top: 1px solid rgba(79,168,255,.11); color: #5f809c; font-size: 8px; }.list-footer span { margin-left: auto; color: #44daa7; }
.detail-id { display: grid; grid-template-columns: 44px 1fr auto; align-items: center; gap: 10px; padding: 12px; border: 1px solid rgba(79,168,255,.14); border-radius: 6px; background: rgba(18,52,91,.3); }.detail-id > span:first-child { display: grid; place-items: center; width: 42px; height: 42px; border-radius: 6px; color: #4cb9ff; background: rgba(22,119,255,.13); }.detail-id div { display: flex; flex-direction: column; }.detail-id small, .detail-grid small { color: #607f9c; font-size: 8px; }.detail-id b { margin-top: 4px; color: #e6f2fc; font-size: 12px; }.detail-grid { display: grid; grid-template-columns: repeat(2,1fr); gap: 8px; margin: 10px 0; }.detail-grid div { display: flex; flex-direction: column; padding: 10px; border: 1px solid rgba(79,168,255,.1); border-radius: 5px; background: rgba(18,52,91,.2); }.detail-grid b { margin-top: 5px; color: #d8e8f5; font-size: 12px; }.primary-action { display: flex; align-items: center; justify-content: center; gap: 6px; width: 100%; height: 36px; border: 1px solid #218bff; border-radius: 5px; color: white; background: linear-gradient(90deg,#1266da,#188ff5); cursor: pointer; }
@media (max-width: 1200px) { .equipment-layout { grid-template-columns: 120px minmax(480px,1fr) 300px; }.model-marker > b { display: none; }.model-status span:not(:first-child) { display: none; } }
@media (max-width: 950px) { .equipment-layout { grid-template-columns: 110px 1fr; }.device-list-panel { grid-column: 1 / -1; min-height: 420px; }.device-list { display: grid; grid-template-columns: repeat(2,1fr); gap: 6px; }.device-list > button { margin: 0; } }
@media (max-width: 680px) { .equipment-layout { grid-template-columns: 1fr; }.side-rail { display: grid; grid-template-columns: repeat(5,1fr); overflow-x: auto; }.side-rail :deep(button) { justify-content: center; padding: 7px; }.side-rail :deep(button span) { display: none; }.twin-panel, .device-list-panel { min-height: auto; }.model-stage { min-height: 440px; }.device-list { grid-template-columns: 1fr; }.twin-toolbar b, .model-hint { display: none; }.model-marker:nth-of-type(n+5) { display: none; } }
</style>
