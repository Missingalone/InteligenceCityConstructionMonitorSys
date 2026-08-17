import { http, type ApiResult } from './http'

export interface ProjectRecord { id:number;projectCode:string;projectName:string;enterpriseId:number;enterpriseName?:string;supervisorOrgId:number;projectType:string;projectStatus:string;address?:string;longitude?:number;latitude?:number;plannedStartDate?:string;plannedEndDate?:string;actualStartDate?:string;progressPercent?:number;projectManager?:string;managerMobile?:string;description?:string;members?:Array<{id:number;projectId:number;userId:number;memberRole:string}> }
export type ProjectPayload = Omit<ProjectRecord,'id'|'enterpriseName'|'members'> & {id?:number}
export interface EnterpriseProjectDetail {
  id:number
  projectCode:string
  projectName:string
  enterpriseName?:string
  supervisorName?:string
  projectType:string
  projectStatus:string
  address?:string
  longitude?:number
  latitude?:number
  plannedStartDate?:string
  plannedEndDate?:string
  actualStartDate?:string
  actualEndDate?:string
  progressPercent?:number
  projectManager?:string
  projectManagerPhone?:string
  description?:string
  createTime?:string
}
export interface EnterpriseProjectUpdatePayload {
  id:number
  projectCode:string
  projectName:string
  enterpriseName?:string
  supervisorName?:string
  projectType:string
  projectStatus:string
  address?:string
  projectManager?:string
  progressPercent?:number
  projectManagerPhone?:string
  description?:string
}
export interface EnterpriseRecord {id:number;organizationId?:number;enterpriseName:string;unifiedSocialCreditCode?:string;legalRepresentative?:string;contactName?:string;contactMobile?:string;address?:string;qualificationInfo?:string;status?:number}

export const getProjectList=()=>http.get<never,ApiResult<ProjectRecord[]>>('/supervisor/projects')
export const getProjectDetail=(id:number)=>http.get<never,ApiResult<ProjectRecord>>(`/supervisor/projects/${id}`)
export const createProject=(data:ProjectPayload)=>http.post<never,ApiResult<number>>('/supervisor/projects',data)
export const updateProject=(data:ProjectPayload)=>http.put<never,ApiResult<void>>('/supervisor/projects',data)
export const deleteProject=(id:number)=>http.delete<never,ApiResult<void>>(`/supervisor/projects/${id}`)
/** 企业端项目详情、修改、删除，与 /enterprise/project 控制器逐字段对应。 */
export const getEnterpriseProjectDetail=(projectId:number)=>http.get<never,ApiResult<EnterpriseProjectDetail>>(`/enterprise/project/details/${projectId}`)
export const updateEnterpriseProject=(data:EnterpriseProjectUpdatePayload)=>http.put<never,ApiResult<boolean>>('/enterprise/project/update',data)
export const deleteEnterpriseProject=(projectId:number)=>http.delete<never,ApiResult<boolean>>(`/enterprise/project/delete/${projectId}`)
export const replaceProjectMembers=(id:number,members:Array<{userId:number;memberRole:string}>)=>http.put<never,ApiResult<void>>(`/supervisor/projects/${id}/members`,members)
export const getEnterpriseList=()=>http.get<never,ApiResult<EnterpriseRecord[]>>('/supervisor/enterprises')
export const getEnterpriseDetail=(id:number)=>http.get<never,ApiResult<EnterpriseRecord>>(`/supervisor/enterprises/${id}`)
export const createEnterprise=(data:Omit<EnterpriseRecord,'id'>)=>http.post<never,ApiResult<number>>('/supervisor/enterprises',data)
export const updateEnterprise=(data:EnterpriseRecord)=>http.put<never,ApiResult<void>>('/supervisor/enterprises',data)
export const deleteEnterprise=(id:number)=>http.delete<never,ApiResult<void>>(`/supervisor/enterprises/${id}`)
