import { http, type ApiResult } from './http'
export interface AlarmRecordRaw {id:number;alarmNo:string;projectId:number;deviceId:number;ruleId:number;alarmType:string;alarmLevel:string;alarmTitle:string;alarmContent:string;thresholdValue?:number;actualValue?:number;alarmStatus:string;triggeredAt:string;handledAt?:string;handleRemark?:string}
export interface AlarmRule {id:number;ruleName:string;ruleCode:string;deviceType?:string;metricName:string;comparisonOperator:string;thresholdValue:number;alarmLevel:string;enabled:number;remark?:string}
export type AlarmRulePayload=Omit<AlarmRule,'id'>&{id?:number}
export interface Rectification {id:number;orderNo:string;projectId:number;alarmId:number;enterpriseId:number;title:string;content:string;deadlineAt:string;status:string;issuedBy:number;issuedAt:string;submittedAt?:string;resultDescription?:string;evidenceUrls?:string;reviewedAt?:string;reviewRemark?:string}
export const getAlarms=()=>http.get<never,ApiResult<AlarmRecordRaw[]>>('/supervisor/alarms')
export const handleAlarm=(id:number,data:{handleRemark:string;resolved:boolean})=>http.put<never,ApiResult<void>>(`/supervisor/alarms/${id}/handle`,data)
export const closeAlarm=(id:number,data:{closeRemark:string})=>http.put<never,ApiResult<void>>(`/supervisor/alarms/${id}/close`,data)
export const getAlarmRules=()=>http.get<never,ApiResult<AlarmRule[]>>('/supervisor/alarm-rules')
export const createAlarmRule=(data:AlarmRulePayload)=>http.post<never,ApiResult<number>>('/supervisor/alarm-rules',data)
export const updateAlarmRule=(data:AlarmRulePayload)=>http.put<never,ApiResult<void>>('/supervisor/alarm-rules',data)
export const deleteAlarmRule=(id:number)=>http.delete<never,ApiResult<void>>(`/supervisor/alarm-rules/${id}`)
export const getRectifications=()=>http.get<never,ApiResult<Rectification[]>>('/supervisor/rectifications')
export const issueRectification=(data:{alarmId:number;title:string;content:string;deadlineAt:string})=>http.post<never,ApiResult<number>>('/supervisor/rectifications',data)
export const reviewRectification=(id:number,data:{approved:boolean;reviewRemark:string})=>http.put<never,ApiResult<void>>(`/supervisor/rectifications/${id}/review`,data)

