import { getToken } from './http'
interface TokenClaims {roles?:string[];permissions?:string[];sub?:string}
const decode=():TokenClaims=>{try{const token=getToken();if(!token)return{};const part=token.split('.')[1];return JSON.parse(decodeURIComponent(escape(atob(part.replace(/-/g,'+').replace(/_/g,'/'))))) as TokenClaims}catch{return{}}}
export const getCurrentAccess=()=>decode()
export const hasPermission=(permission?:string)=>!permission||decode().roles?.includes('ADMIN')===true||decode().permissions?.includes(permission)===true
export const hasAnyRole=(roles?:string[])=>!roles?.length||decode().roles?.includes('ADMIN')===true||roles.some(role=>decode().roles?.includes(role))
