import { http, type ApiResult } from './http'
export interface DeviceRecordRaw {id:number;deviceCode:string;deviceName:string;projectId:number;deviceType:string;manufacturer?:string;model?:string;installationLocation?:string;status:string;lastOnlineAt?:string;installedAt?:string;remark?:string}
export type DevicePayload=Omit<DeviceRecordRaw,'id'|'lastOnlineAt'>&{id?:number}
export const getDeviceRecords=()=>http.get<never,ApiResult<DeviceRecordRaw[]>>('/supervisor/devices')
export const getDeviceDetail=(id:number)=>http.get<never,ApiResult<DeviceRecordRaw>>(`/supervisor/devices/${id}`)
export const createDevice=(data:DevicePayload)=>http.post<never,ApiResult<number>>('/supervisor/devices',data)
export const updateDevice=(data:DevicePayload)=>http.put<never,ApiResult<void>>('/supervisor/devices',data)
export const deleteDevice=(id:number)=>http.delete<never,ApiResult<void>>(`/supervisor/devices/${id}`)

