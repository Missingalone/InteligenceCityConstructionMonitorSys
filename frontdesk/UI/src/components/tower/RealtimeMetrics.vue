<script setup lang="ts">
import { ArrowDownToLine, CircleGauge, Gauge, RotateCw, Weight, Wind } from 'lucide-vue-next'
import type { RealtimeMetric } from '@/api/towerTypes'
const icons: Record<string, unknown> = { weight: Weight, radius: CircleGauge, tilt: Gauge, wind: Wind, torque: RotateCw, height: ArrowDownToLine }
defineProps<{ metrics: RealtimeMetric[]; loading?: boolean }>()
</script>
<template>
  <article class="tech-panel realtime-panel"><div class="panel-head"><h2 class="panel-title">实时工作数据</h2><span class="panel-meta">3 秒刷新</span></div>
    <div v-if="loading" class="loading-state">实时数据加载中...</div>
    <div v-else class="realtime-grid"><div v-for="item in metrics" :key="item.key"><span class="data-icon"><component :is="icons[item.key]" :size="17" /></span><p><small>{{ item.label }}</small><b>{{ item.value }}<em>{{ item.unit }}</em></b></p><span :class="item.status === '关注' ? 'warning' : 'safe'">{{ item.status }}</span></div></div>
  </article>
</template>
<style scoped>
.realtime-grid{display:grid;grid-template-columns:repeat(3,1fr);gap:9px;padding:14px}.realtime-grid>div{display:grid;grid-template-columns:34px 1fr auto;align-items:center;min-height:64px;padding:9px;border:1px solid rgba(50,148,255,.2);border-radius:6px;background:linear-gradient(90deg,rgba(22,119,255,.13),rgba(22,119,255,.03))}.data-icon{display:grid;place-items:center;width:31px;height:31px;border-radius:5px;color:#4daeff;background:rgba(22,119,255,.15)}p{display:flex;flex-direction:column;margin:0}small{color:#62809d;font-size:8px}b{margin-top:2px;color:#dcecff;font-size:17px}em{margin-left:2px;color:#6b93b5;font-size:9px;font-style:normal}.safe,.warning{padding:3px 5px;border-radius:3px;color:#43dba6;background:rgba(54,211,153,.1);font-size:8px}.warning{color:#fbbf24;background:rgba(251,191,36,.1)}.loading-state{display:grid;min-height:150px;place-items:center;color:#7190ad;font-size:11px}@media(max-width:820px){.realtime-grid{grid-template-columns:repeat(2,1fr)}}@media(max-width:600px){.realtime-grid{grid-template-columns:1fr}}
</style>

