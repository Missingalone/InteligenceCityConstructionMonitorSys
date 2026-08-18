<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { confirmTowerAlarm, getTowerAlarmDetail } from '@/api/tower'
import type { TowerAlarmDetail, TowerAlarmStatus } from '@/api/towerTypes'
const props=defineProps<{ modelValue:boolean; alarmId:string }>()
const emit=defineEmits<{ 'update:modelValue':[value:boolean]; updated:[] }>()
const detail=ref<TowerAlarmDetail>(); const loading=ref(false); const error=ref(''); const saving=ref(false); const status=ref<TowerAlarmStatus>('未处理'); const opinion=ref('')
const visible=computed({get:()=>props.modelValue,set:v=>emit('update:modelValue',v)})
const load=async()=>{if(!props.alarmId)return;loading.value=true;error.value='';try{detail.value=await getTowerAlarmDetail(props.alarmId);status.value=detail.value.status;opinion.value=detail.value.opinion==='—'?'':detail.value.opinion}catch(e){error.value=e instanceof Error?e.message:'加载失败'}finally{loading.value=false}}
const save=async()=>{saving.value=true;try{await confirmTowerAlarm(props.alarmId,{status:status.value,opinion:opinion.value});ElMessage.success('报警状态已更新');emit('updated');visible.value=false}finally{saving.value=false}}
watch(()=>[props.modelValue,props.alarmId],()=>{if(props.modelValue)void load()})
</script>
<template><el-dialog v-model="visible" title="报警详情" width="min(820px, 92vw)" destroy-on-close>
  <div v-if="loading" class="dialog-state">报警详情加载中...</div><div v-else-if="error" class="dialog-state error">{{ error }}<button @click="load">重新加载</button></div><div v-else-if="detail" class="detail-layout">
    <div class="detail-grid"><div><span>报警类型</span><b>{{ detail.type }}</b></div><div><span>报警时间</span><b>{{ detail.time }}</b></div><div><span>设备编号</span><b>{{ detail.deviceId }}</b></div><div><span>报警位置</span><b>{{ detail.location }}</b></div><div><span>当前监测值</span><b class="danger">{{ detail.currentValue }}</b></div><div><span>预警阈值</span><b>{{ detail.warningThreshold }}</b></div><div><span>报警阈值</span><b>{{ detail.alarmThreshold }}</b></div><div><span>处理时间</span><b>{{ detail.handledAt }}</b></div></div>
    <div class="screenshot"><img :src="detail.screenshot" alt="关联视频截图"><span>关联视频截图 · {{ detail.deviceName }}</span></div>
    <div class="handle-form"><label>报警状态<select v-model="status"><option>未处理</option><option>处理中</option><option>已处理</option><option>误报</option></select></label><label>处理人<input :value="detail.handler" disabled></label><label class="full">处理意见<textarea v-model="opinion" rows="3" placeholder="请输入处理意见" /></label></div>
  </div><div v-else class="dialog-state">暂无报警详情</div>
  <template #footer><button class="tech-button secondary" @click="visible=false">关闭</button><button class="tech-button" :disabled="loading||saving||!detail" @click="save">{{ saving?'提交中...':'确认处理' }}</button></template>
</el-dialog></template>
<style scoped>
.detail-layout{display:grid;gap:13px}.detail-grid{display:grid;grid-template-columns:repeat(4,1fr);gap:8px}.detail-grid>div{padding:10px;border:1px solid rgba(79,168,255,.13);border-radius:5px;background:rgba(9,31,56,.55)}.detail-grid span{display:block;color:#6686a2;font-size:10px}.detail-grid b{display:block;margin-top:5px;color:#d6e8f7;font-size:11px}.detail-grid .danger{color:#ff697b}.screenshot{position:relative;height:180px;overflow:hidden;border:1px solid rgba(79,168,255,.2);border-radius:6px;background:#061526}.screenshot img{width:100%;height:100%;object-fit:cover;opacity:.58}.screenshot span{position:absolute;right:10px;bottom:8px;padding:4px 7px;color:#bfe5ff;background:rgba(4,17,31,.75);font-size:9px}.handle-form{display:grid;grid-template-columns:1fr 1fr;gap:10px}.handle-form label{display:grid;gap:5px;color:#718fab;font-size:10px}.handle-form .full{grid-column:1/-1}select,input,textarea{width:100%;border:1px solid rgba(79,168,255,.2);border-radius:5px;outline:0;color:#dcecff;background:#091d34;padding:8px;font-size:11px}textarea{resize:vertical}.dialog-state{display:grid;min-height:300px;place-items:center;color:#718fab}.dialog-state.error{color:#ff7b8a}.dialog-state button{padding:6px 10px;border:1px solid #1677ff;color:#9bd4ff;background:rgba(22,119,255,.15)}@media(max-width:700px){.detail-grid{grid-template-columns:repeat(2,1fr)}.handle-form{grid-template-columns:1fr}}
</style>
