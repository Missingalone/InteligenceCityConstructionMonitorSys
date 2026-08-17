import type { Camera, CameraStream, HistoryData, MaintenanceRecord, MapPoint, OperationLog, TowerAlarm, TowerAlarmDetail, TowerAlarmStatus, TowerDevice, TowerDeviceDetail, TowerRealtimeData } from './towerTypes'
import monitoringPoster from '@/assets/construction-digital-twin.png'
import { http, type ApiResult } from './http'
import { getAlarms, handleAlarm, closeAlarm } from './alarm'
import { getSystemOperationLogs } from './system'
import { getDeviceDetail } from './device'

const delay = (ms = 260) => new Promise((resolve) => window.setTimeout(resolve, ms))
const clone = <T>(data: T): T => JSON.parse(JSON.stringify(data)) as T

interface SupervisorDeviceRaw {
  id: number | string
  deviceCode?: string
  deviceName?: string
  deviceType?: string
  status?: string
  operatorName?: string
  currentOperator?: string
  installationLocation?: string
  installationAddress?: string
}

interface CameraRaw {
  id: number | string
  cameraCode?: string
  code?: string
  cameraName?: string
  name?: string
  projectName?: string
  project?: { projectName?: string; name?: string }
  deviceName?: string
  towerName?: string
  foundationPitName?: string
  installationAddress?: string
  installationLocation?: string
  installLocation?: string
  position?: string
  status?: string | number | boolean
  onlineStatus?: string | number | boolean
  lastOnlineAt?: string
  lastOnlineTime?: string
}

interface CameraPageRaw {
  records?: CameraRaw[]
  rows?: CameraRaw[]
  list?: CameraRaw[]
  content?: CameraRaw[]
  total?: number
}

let devices: TowerDevice[] = []
let alarms: TowerAlarm[] = []

const isTowerCrane = (type?: string) => {
  const normalized = String(type ?? '').toUpperCase()
  return normalized === 'TOWER_CRANE' || normalized.includes('TOWER') || normalized.includes('塔吊')
}

export async function getTowerDeviceList(): Promise<TowerDevice[]> {
  const response = await http.get<never, ApiResult<SupervisorDeviceRaw[]>>('/supervisor/devices')
  devices = response.data.filter((item) => isTowerCrane(item.deviceType)).map((item) => ({
    id: String(item.id),
    code: item.deviceCode || `TC-${item.id}`,
    name: item.deviceName || `塔吊设备 ${item.id}`,
    status: item.status === 'ONLINE' || item.status === '运行' || item.status === '在线' ? '在线' : '离线',
    operator: item.operatorName || item.currentOperator || '未排班',
    area: item.installationLocation || '未配置安装位置',
  }))
  return clone(devices)
}
export async function getTowerRealtimeData(deviceId: string): Promise<TowerRealtimeData> {
  await delay(160); const seed = Number(deviceId.slice(-1)) || 2
  return { weight: 10.25 + seed, windSpeed: 5.8 + seed / 2, rotation: 35 + seed * 4, torque: 580 + seed * 16.25,
    metrics: [
      { key: 'weight', label: '吊重', value: 10.25 + seed, unit: 'T', status: '安全' }, { key: 'radius', label: '幅度', value: 50, unit: 'm', status: '正常' },
      { key: 'tilt', label: '倾度', value: .26, unit: '°', status: '正常' }, { key: 'wind', label: '风速', value: 5.8 + seed / 2, unit: 'm/s', status: '正常' },
      { key: 'torque', label: '力矩', value: 580 + seed * 16.25, unit: 'm·T', status: '62%' }, { key: 'height', label: '高度', value: 36.37, unit: 'm', status: '正常' },
    ], torqueSeries: [302,328,316,344,331,372,358,386].map(v => v + seed * 4), weightSeries: [8.2,11.4,13.1,9.8,15.6,12.2,14.8,10.25 + seed],
    workMetrics: [{ label:'工作时长',value:'6.75',unit:'h'},{ label:'今日吊次',value:'29',unit:'次'},{ label:'今日吊重',value:'122.03',unit:'T'},{ label:'今日功效',value:'63.14',unit:'T/min'},{ label:'今日报警',value:String(alarms.filter(a=>a.deviceId===deviceId).length),unit:'次'}] }
}
const alarmLevelMap:Record<string,TowerAlarm['level']>={LOW:'提示',MEDIUM:'一般',HIGH:'严重',CRITICAL:'紧急'}
const alarmStatusMap:Record<string,TowerAlarm['status']>={PENDING:'未处理',HANDLING:'处理中',RESOLVED:'已处理',CLOSED:'已处理'}
export async function getTowerAlarmList(params: { deviceId?: string; level?: string; status?: string } = {}) { const response=await getAlarms();alarms=response.data.map(a=>({id:String(a.id),type:a.alarmTitle||a.alarmType,level:alarmLevelMap[a.alarmLevel]||'一般',deviceId:String(a.deviceId),deviceName:devices.find(d=>d.id===String(a.deviceId))?.name||`设备 ${a.deviceId}`,time:a.triggeredAt,currentValue:a.actualValue==null?'—':String(a.actualValue),threshold:a.thresholdValue==null?'—':String(a.thresholdValue),status:alarmStatusMap[a.alarmStatus]||'未处理',location:`项目 ${a.projectId}`}));return clone(alarms.filter(a => (!params.deviceId || a.deviceId === params.deviceId) && (!params.level || a.level === params.level) && (!params.status || a.status === params.status))) }
export async function getTowerAlarmDetail(alarmId: string): Promise<TowerAlarmDetail> { const alarm = alarms.find(a=>a.id===alarmId); if (!alarm) throw new Error('未找到报警记录'); return { ...clone(alarm), warningThreshold:'后端未返回', alarmThreshold:alarm.threshold, screenshot:monitoringPoster, handler:alarm.status==='未处理'?'待处理':'当前登录用户', opinion:'请以报警处理记录为准', handledAt:'后端详情接口未提供' } }
export async function confirmTowerAlarm(alarmId: string, data: { status: TowerAlarmStatus; opinion?: string }) { const id=Number(alarmId);if(data.status==='误报')throw new Error('后端暂不支持误报状态');if(data.status==='已处理')await handleAlarm(id,{handleRemark:data.opinion||'已处理',resolved:true});else if(data.status==='处理中')await handleAlarm(id,{handleRemark:data.opinion||'处理中',resolved:false});else if(data.status==='未处理')return {success:true};else await closeAlarm(id,{closeRemark:data.opinion||'关闭报警'});return { success:true } }
export async function getTowerHistoryData(_params: { deviceId: string; range: string; start?: string; end?: string }): Promise<HistoryData> { await delay(420); const labels = ['08:00','10:00','12:00','14:00','16:00','18:00','20:00']; return { labels, weight:[8.2,11.4,13.1,9.8,15.6,12.2,14.8], torque:[302,428,516,444,631,572,486], wind:[4.2,5.1,6.8,7.3,6.4,5.8,4.9], rotation:[18,76,122,188,236,292,338], onlineRate:[100,99,100,98,100,100,99], alarms:clone(alarms), summary:{maxLoad:'15.6 T',maxWind:'7.3 m/s',avgTorque:'482 m·T',onlineRate:'99.4%'} } }
export async function exportTowerHistoryData(params: { deviceId: string; range: string }) { await delay(500); const rows = ['time,weight,torque,wind,rotation','08:00,8.2,302,4.2,18','10:00,11.4,428,5.1,76']; return new Blob([`\ufeff${rows.join('\n')}\n# device=${params.deviceId}, range=${params.range}`],{type:'text/csv;charset=utf-8'}) }
let cameras: Camera[] = []
const isOnline = (status?: string | number | boolean) => status === true || status === 1 || ['ONLINE','在线','运行','NORMAL'].includes(String(status ?? '').toUpperCase())
const formatCameraTime = (value?: string) => value ? new Date(value).toLocaleString('zh-CN', { hour12: false }) : '暂无上报'

export async function getCameraList(params: { deviceId?: string; pageNum?: number; pageSize?: number } = {}): Promise<Camera[]> {
  const response = await http.get<never, ApiResult<CameraPageRaw | CameraRaw[]>>('/supervisor/camera/page', {
    params: { pageNum: params.pageNum ?? 1, pageSize: params.pageSize ?? 20 },
  })
  const page = response.data
  const records = Array.isArray(page) ? page : page.records ?? page.rows ?? page.list ?? page.content ?? []
  cameras = records.map((item) => ({
    id: String(item.id),
    code: item.cameraCode || item.code || `CAM-${item.id}`,
    name: item.cameraName || item.name || `摄像头 ${item.id}`,
    project: item.projectName || item.project?.projectName || item.project?.name || '未关联项目',
    target: item.deviceName || item.towerName || item.foundationPitName || '未关联设备',
    position: item.installationAddress || item.installationLocation || item.installLocation || item.position || '未配置安装位置',
    status: isOnline(item.onlineStatus ?? item.status) ? '在线' : '离线',
    lastOnlineAt: formatCameraTime(item.lastOnlineAt || item.lastOnlineTime),
  }))
  const selectedDevice = devices.find((device) => device.id === params.deviceId)
  const related=params.deviceId && selectedDevice ? cameras.filter((camera) => camera.target.includes(selectedDevice.name) || camera.target.includes(selectedDevice.code)) : cameras
  return clone(related.length ? related : cameras)
}
export async function getCameraStream(cameraId: string): Promise<CameraStream> { await delay(520); const camera=cameras.find(c=>c.id===cameraId); return { cameraId, status:camera?.status==='在线'?'ready':'offline', poster:monitoringPoster } }
export async function getTowerDeviceDetail(deviceId: string): Promise<TowerDeviceDetail> { const response=await getDeviceDetail(Number(deviceId));const raw=response.data;const device=devices.find(d=>d.id===deviceId);return { id:String(raw.id),code:raw.deviceCode,name:raw.deviceName,model:raw.model||'未配置',type:raw.deviceType,maxLoad:'后端未提供',maxRadius:'后端未提供',maxHeight:'后端未提供',manufacturer:raw.manufacturer||'未配置',installedAt:raw.installedAt||'未配置',lastMaintenanceAt:'后端未提供',nextMaintenanceAt:'后端未提供',operator:device?.operator||'未排班',project:`项目 ${raw.projectId}`,status:raw.status } }
export async function getMaintenanceRecords(_deviceId: string): Promise<MaintenanceRecord[]> { return [] }
export async function getProjectMapData(_projectId: string): Promise<MapPoint[]> { await delay(); return clone([{id:'p1',type:'project',name:'智慧新城二期',code:'PRJ-2025-02',status:'施工中',x:49,y:52},{id:'t1',type:'tower',name:'西区塔吊 #2',code:'TC-W-002',status:'在线',x:54,y:46},{id:'c1',type:'camera',name:'吊钩全景摄像机',code:'CAM-W-01',status:'在线',x:60,y:42},{id:'pit1',type:'pit',name:'3# 基坑',code:'PIT-003',status:'正常',x:35,y:64},{id:'a1',type:'alarm',name:'力矩超限报警',code:'TC-W-002',status:'紧急',x:56,y:48}]) }
export async function getOperationLogs(_params: Record<string, unknown> = {}): Promise<OperationLog[]> { const response=await getSystemOperationLogs({page:1,size:100});return response.data.records.map(x=>({id:String(x.id),operator:x.username,time:x.createdAt,type:x.moduleName,target:x.requestUri,content:x.operationName,ip:x.clientIp,result:x.responseCode>=200&&x.responseCode<400?'成功':'失败'})) }

export type { Camera, HistoryData, MaintenanceRecord, MapPoint, OperationLog, TowerAlarm, TowerAlarmDetail, TowerDevice, TowerDeviceDetail, TowerRealtimeData } from './towerTypes'
