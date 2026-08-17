import { http, type ApiResult } from './http'
export interface MonitorRecord {id:number;deviceId:number;projectId:number;pm25?:number;pm10?:number;noiseDb?:number;temperature?:number;humidity?:number;windSpeed?:number;windDirection?:string;collectedAt:string}
export const getMonitorData=()=>http.get<never,ApiResult<MonitorRecord[]>>('/supervisor/monitor-data')

