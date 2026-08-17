import { http,type ApiResult } from './http'
export interface OperationLogRaw {id:number;userId:number;username:string;moduleName:string;operationName:string;requestMethod:string;requestUri:string;requestParams?:string;responseCode:number;clientIp:string;executionTimeMs:number;createdAt:string}
export interface PageResult<T>{records:T[];total:number;current:number;size:number}
export const getSystemOperationLogs=(params:{page?:number;size?:number;username?:string;module?:string}={})=>http.get<never,ApiResult<PageResult<OperationLogRaw>>>('/system/operation-logs',{params:{page:params.page??1,size:params.size??20,username:params.username||undefined,module:params.module||undefined}})
export const changePassword=(data:{oldPassword:string;newPassword:string})=>http.put<never,ApiResult<void>>('/auth/account/password',data)

