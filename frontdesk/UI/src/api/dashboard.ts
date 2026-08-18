import { http, type ApiResult } from './http'
export interface DashboardStats {projectCount:number;onlineDeviceCount:number;pendingAlarmCount:number;activeRectificationCount:number;pendingFeedbackCount:number}
export const getAdminDashboard=()=>http.get<never,ApiResult<DashboardStats>>('/admin/dashboard')

