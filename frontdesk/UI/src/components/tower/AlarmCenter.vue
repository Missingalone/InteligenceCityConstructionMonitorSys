<script setup lang="ts">
import { computed, ref } from 'vue'
import { CheckCircle2, Eye, ShieldAlert } from 'lucide-vue-next'
import type { TowerAlarm, TowerAlarmLevel } from '@/api/towerTypes'
const props = defineProps<{ alarms: TowerAlarm[]; loading?: boolean }>()
const emit = defineEmits<{ detail: [alarm: TowerAlarm]; confirm: [alarm: TowerAlarm] }>()
const selectedLevel = ref<'全部' | TowerAlarmLevel>('全部')
const levels: Array<'全部' | TowerAlarmLevel> = ['全部','提示','一般','严重','紧急']
const filtered = computed(() => selectedLevel.value === '全部' ? props.alarms : props.alarms.filter(a => a.level === selectedLevel.value))
const count = (status: string) => props.alarms.filter(a => a.status === status).length
</script>
<template>
  <article class="tech-panel alarm-center"><div class="panel-head"><h2 class="panel-title">实时报警中心</h2><span class="panel-meta"><i /> 实时更新</span></div>
    <div class="alarm-stats"><div><small>报警总数</small><b>{{ alarms.length }}</b></div><div><small>未处理</small><b class="red">{{ count('未处理') }}</b></div><div><small>处理中</small><b class="amber">{{ count('处理中') }}</b></div><div><small>已处理</small><b class="green">{{ count('已处理') }}</b></div></div>
    <div class="level-filters"><button v-for="level in levels" :key="level" :class="{active:selectedLevel===level}" @click="selectedLevel=level">{{ level }}</button></div>
    <div v-if="loading" class="center-state">报警数据加载中...</div><div v-else-if="!filtered.length" class="center-state">当前筛选下暂无报警</div>
    <div v-else class="alarm-list"><div v-for="alarm in filtered" :key="alarm.id" class="alarm-item" :class="[`level-${alarm.level}`,{important:alarm.level==='紧急'&&alarm.status==='未处理'}]" @click="emit('detail',alarm)">
      <div class="alarm-line"><span class="level-tag">{{ alarm.level }}</span><strong>{{ alarm.type }}</strong><span class="status-tag">{{ alarm.status }}</span></div>
      <p>{{ alarm.deviceName }} · {{ alarm.time }}</p><p>当前 <b>{{ alarm.currentValue }}</b><span>阈值 {{ alarm.threshold }}</span></p>
      <div class="alarm-actions"><button @click.stop="emit('detail',alarm)"><Eye :size="12" />查看详情</button><button :disabled="alarm.status==='已处理'" @click.stop="emit('confirm',alarm)"><CheckCircle2 :size="12" />确认处理</button></div>
    </div></div>
  </article>
</template>
<style scoped>
.alarm-center{min-height:430px}.panel-meta{display:flex;align-items:center;gap:5px}.panel-meta i{width:6px;height:6px;border-radius:50%;background:#36d399;box-shadow:0 0 7px #36d399}.alarm-stats{display:grid;grid-template-columns:repeat(4,1fr);gap:6px;padding:10px}.alarm-stats>div{padding:8px 6px;border:1px solid rgba(79,168,255,.12);border-radius:5px;background:rgba(12,44,77,.48);text-align:center}.alarm-stats small{display:block;color:#6686a2;font-size:8px}.alarm-stats b{display:block;margin-top:3px;color:#dceeff;font-size:17px}.alarm-stats .red{color:#ff6478}.alarm-stats .amber{color:#ffad41}.alarm-stats .green{color:#42dca8}.level-filters{display:flex;gap:5px;padding:0 10px 9px}.level-filters button,.alarm-actions button{border:1px solid rgba(79,168,255,.14);border-radius:4px;color:#7594b0;background:rgba(18,52,91,.35);cursor:pointer;transition:.2s}.level-filters button{flex:1;padding:5px 2px;font-size:8px}.level-filters button:hover,.level-filters button.active{color:#fff;border-color:#258cff;background:rgba(22,119,255,.25)}.alarm-list{max-height:390px;padding:0 10px 10px;overflow:auto}.alarm-item{position:relative;margin-bottom:7px;padding:9px 9px 8px 11px;border:1px solid rgba(79,168,255,.11);border-left:2px solid #45a6ff;border-radius:5px;background:rgba(7,27,50,.52);cursor:pointer}.alarm-item:hover{border-color:rgba(82,176,255,.38);background:rgba(18,57,95,.58)}.alarm-item.level-一般{border-left-color:#ffad41}.alarm-item.level-严重,.alarm-item.level-紧急{border-left-color:#ff5e72}.alarm-item.important{animation:alarm-glow 2s ease-in-out infinite}.alarm-line{display:flex;align-items:center;gap:6px}.alarm-line strong{color:#d9e9f8;font-size:10px}.level-tag,.status-tag{padding:2px 5px;border-radius:3px;color:#58adff;background:rgba(22,119,255,.12);font-size:7px}.level-一般 .level-tag{color:#ffb24d;background:rgba(255,157,38,.12)}.level-严重 .level-tag,.level-紧急 .level-tag{color:#ff6f80;background:rgba(255,94,114,.13)}.status-tag{margin-left:auto;color:#8aa5bd;background:rgba(117,145,170,.1)}.alarm-item p{display:flex;justify-content:space-between;margin:7px 0 0;color:#6e8daa;font-size:8px}.alarm-item p b{color:#d5e7f5}.alarm-actions{display:flex;gap:6px;margin-top:8px}.alarm-actions button{display:flex;align-items:center;justify-content:center;gap:3px;flex:1;padding:5px;font-size:8px}.alarm-actions button:hover:not(:disabled){color:#e9f6ff;border-color:#2d93f5;background:rgba(22,119,255,.2)}.alarm-actions button:disabled{opacity:.35;cursor:not-allowed}.center-state{display:grid;min-height:160px;place-items:center;color:#6d8ba7;font-size:10px}@keyframes alarm-glow{0%,100%{box-shadow:inset 0 0 0 rgba(255,94,114,0)}50%{box-shadow:inset 0 0 18px rgba(255,94,114,.12),0 0 10px rgba(255,94,114,.12)}}
</style>

