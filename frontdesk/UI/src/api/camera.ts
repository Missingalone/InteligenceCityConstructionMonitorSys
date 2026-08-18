import { http, type ApiResult } from './http'
export interface CameraRecord {id:number;cameraName:string;cameraCode:string;cameraType?:string;deviceModel?:string;manufacturer?:string;installationAddress?:string;direction?:string;status:number;remark?:string;createdBy?:number;createdTime?:string}
export interface CameraDetail extends Omit<CameraRecord,'id'>{projectId?:number;foundationPitId?:number;hasAudio?:number;hasPtz?:number;updatedBy?:number;updatedTime?:string}
export type CameraPayload=Omit<CameraDetail,'createdBy'|'createdTime'|'updatedBy'|'updatedTime'>&{id?:number;projectId:number}
export interface PageResult<T>{records:T[];total:number;current:number;size:number;pages?:number}
export const getCameraPage=(pageNum=1,pageSize=10)=>http.get<never,ApiResult<PageResult<CameraRecord>>>('/supervisor/camera/page',{params:{pageNum,pageSize}})
export const getCameraDetail=(id:number)=>http.get<never,ApiResult<CameraDetail>>(`/supervisor/camera/details/${id}`)
export const createCamera=(data:CameraPayload)=>http.post<never,ApiResult<number>>('/supervisor/camera',data)
export const updateCamera=(data:CameraPayload)=>http.put<never,ApiResult<void>>('/supervisor/camera',data)
export const deleteCamera=(id:number)=>http.delete<never,ApiResult<void>>(`/supervisor/camera/${id}`)
