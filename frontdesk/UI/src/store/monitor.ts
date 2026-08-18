import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import { getAlarmList, getDeviceList, getDeviceSummary, getEnvironmentLatest } from '@/api'
import type { AlarmRecord, DeviceRecord, DeviceSummary, EnvironmentMetric } from '@/api'

export const useMonitorStore = defineStore('monitor', () => {
  const loading = ref(false)
  const environment = ref<EnvironmentMetric[]>([])
  const alarms = ref<AlarmRecord[]>([])
  const devices = ref<DeviceRecord[]>([])
  const summaries = ref<DeviceSummary[]>([])
  const lastUpdated = ref('')
  const activeSite = ref('中心城区建设项目')

  const unresolvedAlarmCount = computed(() => alarms.value.filter((item) => item.status !== '已完成').length)

  async function fetchDashboard() {
    loading.value = true
    try {
      const [env, alarm, device, summary] = await Promise.all([
        getEnvironmentLatest(), getAlarmList(), getDeviceList(), getDeviceSummary(),
      ])
      environment.value = env.data
      alarms.value = alarm.data
      devices.value = device.data
      summaries.value = summary.data
      lastUpdated.value = new Date().toLocaleTimeString('zh-CN', { hour12: false })
    } finally {
      loading.value = false
    }
  }

  function simulateRealtime() {
    environment.value = environment.value.map((item) => ({
      ...item,
      value: Math.max(0, Number((item.value + (Math.random() - 0.5) * (item.key.includes('pm') ? 1.8 : 0.5)).toFixed(1))),
      trend: Number((item.trend + (Math.random() - 0.5) * 0.6).toFixed(1)),
    }))
    lastUpdated.value = new Date().toLocaleTimeString('zh-CN', { hour12: false })
  }

  return { loading, environment, alarms, devices, summaries, lastUpdated, activeSite, unresolvedAlarmCount, fetchDashboard, simulateRealtime }
})
