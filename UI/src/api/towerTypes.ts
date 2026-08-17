export type TowerAlarmLevel = '提示' | '一般' | '严重' | '紧急'
export type TowerAlarmStatus = '未处理' | '处理中' | '已处理' | '误报'

export interface TowerDevice { id: string; code: string; name: string; status: '在线' | '离线'; operator: string; area: string }
export interface RealtimeMetric { key: string; label: string; value: number; unit: string; status: string }
export interface TowerRealtimeData { weight: number; windSpeed: number; rotation: number; torque: number; metrics: RealtimeMetric[]; torqueSeries: number[]; weightSeries: number[]; workMetrics: Array<{ label: string; value: string; unit: string }> }
export interface TowerAlarm { id: string; type: string; level: TowerAlarmLevel; deviceId: string; deviceName: string; time: string; currentValue: string; threshold: string; status: TowerAlarmStatus; location: string }
export interface TowerAlarmDetail extends TowerAlarm { warningThreshold: string; alarmThreshold: string; screenshot: string; handler: string; opinion: string; handledAt: string }
export interface TowerDeviceDetail { id: string; code: string; name: string; model: string; type: string; maxLoad: string; maxRadius: string; maxHeight: string; manufacturer: string; installedAt: string; lastMaintenanceAt: string; nextMaintenanceAt: string; operator: string; project: string; status: string }
export interface MaintenanceRecord { id: string; category: '维修记录' | '保养记录' | '年检记录'; inspector: string; date: string; result: string; content: string; nextDate: string; reportStatus: string }
export interface Camera { id: string; code: string; name: string; project: string; target: string; position: string; status: '在线' | '离线' | '无信号'; lastOnlineAt: string }
export interface CameraStream { cameraId: string; status: 'ready' | 'offline' | 'error'; poster: string }
export interface HistoryData { labels: string[]; weight: number[]; torque: number[]; wind: number[]; rotation: number[]; onlineRate: number[]; alarms: TowerAlarm[]; summary: { maxLoad: string; maxWind: string; avgTorque: string; onlineRate: string } }
export interface MapPoint { id: string; type: 'project' | 'tower' | 'camera' | 'pit' | 'alarm'; name: string; code: string; status: string; x: number; y: number }
export interface OperationLog { id: string; operator: string; time: string; type: string; target: string; content: string; ip: string; result: '成功' | '失败' }

