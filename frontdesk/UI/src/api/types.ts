export type DeviceStatus = '运行' | '离线' | '报警'
export type AlarmLevel = '高' | '中' | '低'
export type AlarmStatus = '未处理' | '处理中' | '已完成'

export interface EnvironmentMetric {
  key: string
  label: string
  value: number
  unit: string
  trend: number
  tone: 'blue' | 'cyan' | 'green' | 'amber' | 'rose' | 'violet'
}

export interface AlarmRecord {
  id: number
  deviceId?: number
  content: string
  location: string
  time: string
  level: AlarmLevel
  status: AlarmStatus
}

export interface DeviceRecord {
  id: number
  name: string
  code: string
  type: string
  status: DeviceStatus
  signal: number
  updatedAt: string
}

export interface DeviceSummary {
  type: string
  total: number
  online: number
  alarm: number
}
