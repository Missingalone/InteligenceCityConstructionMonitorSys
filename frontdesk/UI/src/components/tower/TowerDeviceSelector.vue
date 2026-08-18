<script setup lang="ts">
import { ChevronDown, Construction } from 'lucide-vue-next'
import type { TowerDevice } from '@/api/towerTypes'
defineProps<{ devices: TowerDevice[]; modelValue: string; loading?: boolean }>()
defineEmits<{ 'update:modelValue': [value: string] }>()
</script>
<template>
  <div class="crane-selector tech-panel" aria-label="塔吊设备切换">
    <span>监测设备</span>
    <button v-for="device in devices" :key="device.id" :disabled="loading" :class="{ active: modelValue === device.id }" @click="$emit('update:modelValue', device.id)"><Construction :size="15" />{{ device.name }}<i :class="{ offline: device.status === '离线' }" /></button>
    <button class="selector-more" disabled>更多设备 <ChevronDown :size="14" /></button>
    <div class="driver"><span>当班司机</span><b>{{ devices.find(d => d.id === modelValue)?.operator || '—' }} · 已实名</b></div>
  </div>
</template>
<style scoped>
.crane-selector{display:flex;align-items:center;gap:8px;min-height:56px;margin-bottom:12px;padding:8px 13px}.crane-selector>span{margin-right:5px;color:#7694af;font-size:10px}.crane-selector button{display:flex;align-items:center;gap:7px;height:34px;padding:0 12px;border:1px solid rgba(79,168,255,.14);border-radius:5px;color:#7897b4;background:rgba(18,52,91,.34);font-size:10px;cursor:pointer;transition:.2s}.crane-selector button:hover:not(:disabled){color:#fff;border-color:#298eff;background:rgba(22,119,255,.25)}.crane-selector button:disabled{cursor:not-allowed;opacity:.55}.crane-selector button i{width:6px;height:6px;margin-left:3px;border-radius:50%;background:#36d399;box-shadow:0 0 7px #36d399}.crane-selector button i.offline{background:#72879c;box-shadow:none}.crane-selector button.active{color:#eaf5ff;border-color:#1677ff;background:linear-gradient(90deg,rgba(22,119,255,.5),rgba(22,119,255,.2))}.crane-selector .selector-more{border-style:dashed}.driver{display:flex;align-items:center;gap:8px;margin-left:auto;padding-left:15px;border-left:1px solid rgba(79,168,255,.13);font-size:9px}.driver span{color:#617f9a}.driver b{color:#a8bfd4;font-weight:500}@media(max-width:820px){.driver,.selector-more{display:none}}@media(max-width:600px){.crane-selector{overflow-x:auto}.crane-selector>span{display:none}.crane-selector button{white-space:nowrap}}
</style>

