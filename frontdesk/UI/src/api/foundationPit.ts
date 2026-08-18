import { http,type ApiResult } from './http'

export interface FoundationPitRecord {
  id:number;pitCode:string;pitName:string;projectId:number;pitType?:string;excavationMethod?:string;area?:number
  supportType?:string;supportScheme?:string;excavationStartDate?:string;excavationEndDate?:string;backfillDate?:string
  currentStage?:string;progress?:number;riskLevel?:string;monitoringStatus?:number;warningThreshold?:string
  alarmReason?:string;longitude?:number;latitude?:number;locationAddress?:string;responsiblePerson?:string
  responsiblePhone?:string;description?:string;remark?:string;createdTime?:string;updatedTime?:string
}
export type FoundationPitPayload=Omit<FoundationPitRecord,'id'|'createdTime'|'updatedTime'>&{id?:number}
export interface PageResult<T>{records:T[];total:number;current:number;size:number;pages?:number}

export const getFoundationPitPage=(pageNum=1,pageSize=10)=>http.get<never,ApiResult<PageResult<FoundationPitRecord>>>('/supervisor/foundation-pits/page',{params:{pageNum,pageSize}})
export const getFoundationPitDetail=(id:number)=>http.get<never,ApiResult<FoundationPitRecord>>(`/supervisor/foundation-pits/${id}`)
export const createFoundationPit=(data:FoundationPitPayload)=>http.post<never,ApiResult<number>>('/supervisor/foundation-pits',data)
export const updateFoundationPit=(data:FoundationPitPayload)=>http.put<never,ApiResult<void>>('/supervisor/foundation-pits',data)
export const deleteFoundationPit=(id:number)=>http.delete<never,ApiResult<void>>(`/supervisor/foundation-pits/${id}`)
