import { http } from './http'
import type { ApiResult } from './http'
import type { AlarmRecord, DeviceRecord, DeviceSummary, EnvironmentMetric } from './types'

interface LoginResult { token: string; tokenType: string }
interface MonitorDataRaw {
  temperature?: number; humidity?: number; windSpeed?: number; noiseDb?: number
  pm25?: number; pm10?: number; collectedAt?: string
}
interface AlarmRaw {
  id: number; deviceId?: number; projectId?: number; alarmTitle?: string; alarmContent?: string
  alarmLevel?: string; alarmStatus?: string; triggeredAt?: string
}
interface DeviceRaw {
  id: number; deviceCode: string; deviceName: string; deviceType: string
  installationLocation?: string; status: string; lastOnlineAt?: string
}

const deviceTypeLabels: Record<string, string> = {
  TOWER_CRANE: '塔吊', ELEVATOR: '升降机', FORMWORK: '高支模', FOUNDATION_PIT: '深基坑', DUST: '环境监测',
}
const alarmLevelLabels: Record<string, AlarmRecord['level']> = {
  CRITICAL: '高', HIGH: '高', MEDIUM: '中', LOW: '低',
}
const alarmStatusLabels: Record<string, AlarmRecord['status']> = {
  PENDING: '未处理', HANDLING: '处理中', RESOLVED: '已完成', CLOSED: '已完成',
}

const formatRelativeTime = (value?: string) => {
  if (!value) return '暂无上报'
  const elapsed = Date.now() - new Date(value).getTime()
  if (elapsed < 60_000) return '刚刚'
  if (elapsed < 3_600_000) return `${Math.floor(elapsed / 60_000)}分钟前`
  return new Date(value).toLocaleString('zh-CN', { hour12: false })
}

/** Spring Security formLogin 默认接收表单格式用户名和密码。 */
export const login = (username: string, password: string) => {
  const body = new URLSearchParams({ username, password })
  return http.post<never, ApiResult<LoginResult>>('/auth/login', body, {
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
  })
}

export const getEnvironmentLatest = async (): Promise<ApiResult<EnvironmentMetric[]>> => {
  const response = await http.get<never, ApiResult<MonitorDataRaw[]>>('/supervisor/monitor-data')
  const latest = response.data[0] ?? {}
  const data: EnvironmentMetric[] = [
    { key: 'temperature', label: '温度', value: Number(latest.temperature ?? 0), unit: '℃', trend: 0, tone: 'blue' },
    { key: 'humidity', label: '湿度', value: Number(latest.humidity ?? 0), unit: '%RH', trend: 0, tone: 'violet' },
    { key: 'wind', label: '风速', value: Number(latest.windSpeed ?? 0), unit: 'm/s', trend: 0, tone: 'cyan' },
    { key: 'noise', label: '噪声', value: Number(latest.noiseDb ?? 0), unit: 'dB', trend: 0, tone: 'amber' },
    { key: 'pm25', label: 'PM2.5', value: Number(latest.pm25 ?? 0), unit: 'μg/m³', trend: 0, tone: 'green' },
    { key: 'pm10', label: 'PM10', value: Number(latest.pm10 ?? 0), unit: 'μg/m³', trend: 0, tone: 'rose' },
  ]
  return { ...response, data }
}

export const getAlarmList = async (): Promise<ApiResult<AlarmRecord[]>> => {
  const response = await http.get<never, ApiResult<AlarmRaw[]>>('/supervisor/alarms')
  const data = response.data.map((item) => ({
    id: item.id,
    deviceId: item.deviceId,
    content: item.alarmTitle || item.alarmContent || '设备监测告警',
    location: `项目 ${item.projectId ?? '-'}`,
    time: item.triggeredAt ? new Date(item.triggeredAt).toLocaleTimeString('zh-CN', { hour12: false }) : '--:--:--',
    level: alarmLevelLabels[item.alarmLevel ?? ''] ?? '低',
    status: alarmStatusLabels[item.alarmStatus ?? ''] ?? '未处理',
  }))
  return { ...response, data }
}

export const getDeviceList = async (): Promise<ApiResult<DeviceRecord[]>> => {
  const response = await http.get<never, ApiResult<DeviceRaw[]>>('/supervisor/devices')
  const data = response.data.map((item) => ({
    id: item.id,
    name: item.deviceName,
    code: item.deviceCode,
    type: deviceTypeLabels[item.deviceType] ?? item.deviceType,
    status: item.status === 'ONLINE' ? '运行' as const : item.status === 'ALARM' ? '报警' as const : '离线' as const,
    signal: item.status === 'ONLINE' ? 100 : 0,
    updatedAt: formatRelativeTime(item.lastOnlineAt),
  }))
  return { ...response, data }
}

export const getDeviceSummary = async (): Promise<ApiResult<DeviceSummary[]>> => {
  const [deviceResponse, alarmResponse] = await Promise.all([getDeviceList(), getAlarmList()])
  const activeAlarmDeviceIds = new Set(alarmResponse.data.filter((item) => item.status !== '已完成').map((item) => item.deviceId))
  const grouped = new Map<string, DeviceSummary>()
  for (const device of deviceResponse.data) {
    const summary = grouped.get(device.type) ?? { type: device.type, total: 0, online: 0, alarm: 0 }
    summary.total += 1
    if (device.status === '运行') summary.online += 1
    if (activeAlarmDeviceIds.has(device.id)) summary.alarm += 1
    grouped.set(device.type, summary)
  }
  return { code: 200, message: '操作成功', data: [...grouped.values()] }
}

export type { AlarmRecord, DeviceRecord, DeviceSummary, EnvironmentMetric } from './types'
export * from './tower'
