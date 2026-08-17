import axios from 'axios'

export interface ApiResult<T> {
  code: number
  message: string
  data: T
}

const TOKEN_KEY = 'intelligence_city_token'

/** 读取登录成功后保存在浏览器中的 JWT。 */
export const getToken = () => window.localStorage.getItem(TOKEN_KEY)
export const setToken = (token: string) => window.localStorage.setItem(TOKEN_KEY, token)
export const clearToken = () => window.localStorage.removeItem(TOKEN_KEY)

export const http = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? '/api',
  timeout: 10000,
})

http.interceptors.request.use((config) => {
  const token = getToken()
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

http.interceptors.response.use(
  (response) => {
    const result = response.data as ApiResult<unknown>
    // 后端业务错误也使用统一 Result 返回，统一转换为 rejected Promise。
    if (typeof result?.code === 'number' && result.code !== 200) {
      return Promise.reject(new Error(result.message || '请求失败'))
    }
    // 返回原始 data，保留调用层已经声明的 Axios 第二泛型返回类型。
    return response.data
  },
  (error) => {
    if (error.response?.status === 401) {
      clearToken()
      if (window.location.pathname !== '/login') window.location.assign('/login')
    }
    const message = error.response?.data?.message || error.message || '网络请求失败'
    return Promise.reject(new Error(message))
  },
)
