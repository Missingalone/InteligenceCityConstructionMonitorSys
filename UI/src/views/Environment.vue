<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'
import { Activity, BellRing, CloudSun, Droplets, Fan, Gauge, MapPin, Thermometer, Wind, Waves } from 'lucide-vue-next'
import LineChart from '@/charts/LineChart.vue'
import BarChart from '@/charts/BarChart.vue'
import GaugeChart from '@/charts/GaugeChart.vue'
import DonutChart from '@/charts/DonutChart.vue'
import { useMonitorStore } from '@/store/monitor'

const store = useMonitorStore()
const selectedPoint = ref('A-01 主监测站')
const airValue = ref(62)
let timer: number | undefined
const metric = (key: string) => computed(() => store.environment.find((item) => item.key === key)?.value ?? 0)
const temperature = metric('temperature')
const humidity = metric('humidity')
const wind = metric('wind')
const noise = metric('noise')
const pm25 = metric('pm25')
const pm10 = metric('pm10')
const topMetrics = computed(() => [
  { label: '环境温度', value: temperature.value, unit: '℃', icon: Thermometer, color: '#52b6ff', tag: '舒适' },
  { label: '相对湿度', value: humidity.value, unit: '%RH', icon: Droplets, color: '#8b83ff', tag: '正常' },
  { label: '现场风力', value: wind.value, unit: '级', icon: Wind, color: '#22d3ee', tag: '适宜' },
  { label: '环境噪声', value: noise.value, unit: 'dB', icon: Waves, color: '#fbbf24', tag: noise.value > 65 ? '关注' : '正常' },
  { label: 'PM2.5', value: pm25.value, unit: 'μg/m³', icon: CloudSun, color: '#36d399', tag: '优' },
  { label: '综合 AQI', value: airValue.value, unit: '', icon: Activity, color: '#45a3ff', tag: '良' },
])
const records = [
  { sensor: '环境监测传感器 #12', event: 'PM10 瞬时浓度偏高', time: '10:38:19', status: '已恢复' },
  { sensor: '噪声监测仪 #05', event: '噪声超过夜间预警值', time: '09:56:40', status: '处理中' },
  { sensor: '气象站 #02', event: '风速达到三级关注阈值', time: '09:31:04', status: '已确认' },
  { sensor: '扬尘监测仪 #08', event: '喷淋设备自动联动开启', time: '08:47:21', status: '已完成' },
]
onMounted(async () => { if (!store.environment.length) await store.fetchDashboard(); timer = window.setInterval(() => { store.simulateRealtime(); airValue.value = Math.max(30, Math.min(100, airValue.value + Math.round((Math.random() - .5) * 5))) }, 5000) })
onBeforeUnmount(() => window.clearInterval(timer))
</script>

<template>
  <div class="view-page environment-page">
    <div class="page-head">
      <div><p class="page-eyebrow">ENVIRONMENTAL IOT MONITORING</p><h1 class="page-title">施工环境智能监测</h1><p class="page-subtitle">扬尘、噪声与微气象数据统一感知分析</p></div>
      <div class="site-select"><MapPin :size="15" /><select v-model="selectedPoint"><option>A-01 主监测站</option><option>东区材料场监测点</option><option>西区道路监测点</option></select></div>
    </div>

    <div class="environment-kpis">
      <article v-for="item in topMetrics" :key="item.label" class="env-kpi tech-panel" :style="{ '--tone': item.color }">
        <span class="env-kpi-icon"><component :is="item.icon" :size="21" /></span>
        <div><small>{{ item.label }}</small><strong>{{ item.value }}<em>{{ item.unit }}</em></strong></div>
        <span class="env-tag">{{ item.tag }}</span><i class="env-spark" />
      </article>
    </div>

    <div class="environment-grid">
      <article class="tech-panel wide-chart"><div class="panel-head"><h2 class="panel-title">24 小时空气质量趋势</h2><span class="panel-meta">μg/m³ · 每 10 分钟采样</span></div><div class="env-chart"><LineChart :labels="['00时','03时','06时','09时','12时','15时','18时','21时','现在']" :series="[{ name:'PM2.5', data:[18,16,19,24,31,28,25,22,pm25], color:'#36d399', area:true },{ name:'PM10', data:[38,35,40,46,59,54,48,45,pm10], color:'#3b82f6' }]" unit="μg/m³" /></div></article>

      <article class="tech-panel realtime-air"><div class="panel-head"><h2 class="panel-title">空气质量实时评估</h2><span class="panel-meta">AQI {{ airValue }}</span></div><div class="gauges"><GaugeChart :value="pm10" :max="150" name="PM10" unit="" color="#3b82f6" /><GaugeChart :value="pm25" :max="100" name="PM2.5" unit="" color="#36d399" /><GaugeChart :value="56" :max="100" name="TSP" unit="" color="#fbbf24" /></div><div class="air-tip"><CloudSun :size="16" /><span><b>空气质量良好</b> 当前施工环境适宜户外作业</span></div></article>

      <article class="tech-panel history-air"><div class="panel-head"><h2 class="panel-title">历史空气质量分布</h2><span class="panel-meta">近 30 日</span></div><div class="donut-box"><DonutChart :data="[{name:'优',value:11,color:'#36d399'},{name:'良',value:13,color:'#3b82f6'},{name:'轻度污染',value:4,color:'#8b5cf6'},{name:'中度污染',value:2,color:'#f59e0b'}]" center-text="80%" center-subtext="优良天数" /></div></article>

      <article class="tech-panel"><div class="panel-head"><h2 class="panel-title">近一周温湿度趋势</h2><span class="panel-meta">°C / %RH</span></div><div class="env-chart small"><LineChart :labels="['周一','周二','周三','周四','周五','周六','今日']" :series="[{name:'温度',data:[21,23,22,25,24,23,temperature],color:'#f59e0b'},{name:'湿度',data:[58,61,64,60,63,65,humidity],color:'#3b82f6'}]" /></div></article>

      <article class="tech-panel"><div class="panel-head"><h2 class="panel-title">近 7 日空气质量</h2><span class="panel-meta">每日 AQI 峰值</span></div><div class="env-chart small"><BarChart :labels="['周一','周二','周三','周四','周五','周六','今日']" :values="[48,52,76,64,43,58,airValue]" :colors="['#36d399','#36d399','#f59e0b','#3b82f6','#36d399','#3b82f6','#3b82f6']" /></div></article>

      <article class="tech-panel event-panel"><div class="panel-head"><h2 class="panel-title">环境报警与联动记录</h2><span class="panel-meta"><BellRing :size="12" /> 实时推送</span></div><div class="event-list"><div v-for="row in records" :key="row.time"><span class="event-icon"><Fan :size="15" /></span><p><b>{{ row.event }}</b><small>{{ row.sensor }}</small></p><time>{{ row.time }}</time><span class="status-pill" :class="row.status === '处理中' ? 'status-processing' : 'status-done'">{{ row.status }}</span></div></div></article>
    </div>
  </div>
</template>

<style scoped>
.site-select { display: flex; align-items: center; gap: 7px; height: 38px; padding: 0 10px; border: 1px solid rgba(79,168,255,.17); border-radius: 6px; color: #58bfff; background: rgba(10,35,62,.55); }.site-select select { border: 0; outline: 0; color: #a7bed3; background: transparent; font-size: 10px; }.environment-kpis { display: grid; grid-template-columns: repeat(6,1fr); gap: 10px; margin-bottom: 12px; }.env-kpi { display: grid; grid-template-columns: 40px 1fr auto; align-items: center; min-height: 82px; padding: 11px; color: var(--tone); }.env-kpi::after { position: absolute; inset: auto 0 0; height: 2px; content: ''; background: linear-gradient(90deg,transparent,var(--tone),transparent); opacity: .6; }.env-kpi-icon { display: grid; place-items: center; width: 38px; height: 38px; border: 1px solid color-mix(in srgb,var(--tone) 32%,transparent); border-radius: 7px; color: var(--tone); background: color-mix(in srgb,var(--tone) 10%,transparent); }.env-kpi > div { display: flex; min-width: 0; flex-direction: column; }.env-kpi small { color: #6685a0; font-size: 8px; }.env-kpi strong { margin-top: 3px; color: #eaf5ff; font-size: 19px; }.env-kpi em { margin-left: 2px; color: var(--tone); font-size: 8px; font-style: normal; }.env-tag { align-self: start; padding: 3px 5px; border-radius: 3px; color: var(--tone); background: color-mix(in srgb,var(--tone) 9%,transparent); font-size: 7px; }.env-spark { position: absolute; right: 8px; bottom: 8px; width: 38px; height: 13px; opacity: .35; background: linear-gradient(155deg,transparent 0 23%,var(--tone) 24% 28%,transparent 29% 46%,var(--tone) 47% 52%,transparent 53%); }
.environment-grid { display: grid; grid-template-columns: 1.2fr 1.2fr 1fr; gap: 12px; }.env-chart { height: 290px; padding: 5px 8px 9px; }.env-chart.small { height: 245px; }.wide-chart { grid-column: span 2; }.gauges { display: grid; grid-template-columns: repeat(3,1fr); height: 230px; padding: 5px; }.gauges .chart-box { min-height: 190px; }.air-tip { display: flex; align-items: center; gap: 8px; margin: 0 12px 12px; padding: 8px 10px; border: 1px solid rgba(54,211,153,.14); border-radius: 5px; color: #36d399; background: rgba(54,211,153,.06); }.air-tip span { color: #7895af; font-size: 8px; }.air-tip b { display: block; margin-bottom: 2px; color: #bcebdc; }.donut-box { height: 290px; padding: 5px; }.event-list { padding: 4px 12px 10px; }.event-list > div { display: grid; grid-template-columns: 32px 1fr 52px 55px; align-items: center; gap: 8px; min-height: 57px; border-bottom: 1px solid rgba(79,168,255,.09); }.event-icon { display: grid; place-items: center; width: 30px; height: 30px; border-radius: 5px; color: #43b9ff; background: rgba(22,119,255,.1); }.event-list p { display: flex; min-width: 0; flex-direction: column; margin: 0; }.event-list b { overflow: hidden; color: #c5d7e6; font-size: 9px; font-weight: 500; text-overflow: ellipsis; white-space: nowrap; }.event-list small { margin-top: 4px; color: #557491; font-size: 7px; }.event-list time { color: #5c7a97; font-size: 8px; }
@media (max-width: 1200px) { .environment-kpis { grid-template-columns: repeat(3,1fr); }.environment-grid { grid-template-columns: repeat(2,1fr); }.wide-chart { grid-column: span 2; }.history-air { grid-column: span 1; } }
@media (max-width: 720px) { .environment-kpis { grid-template-columns: repeat(2,1fr); }.environment-grid { grid-template-columns: 1fr; }.wide-chart { grid-column: span 1; }.gauges { grid-template-columns: 1fr; height: 600px; }.event-list > div { grid-template-columns: 32px 1fr auto; }.event-list time { display: none; } }
@media (max-width: 420px) { .environment-kpis { grid-template-columns: 1fr; }.env-kpi { min-height: 70px; } }
</style>
