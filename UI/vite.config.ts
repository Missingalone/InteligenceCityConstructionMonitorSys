import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: { '@': fileURLToPath(new URL('./src', import.meta.url)) },
  },
  server: {
    port: 5173,
    proxy: {
      // 开发环境由 Vite 转发到独立后端服务，浏览器侧无需处理跨域。
      '/api/auth': { target: 'http://127.0.0.1:8003', changeOrigin: true, rewrite: (path) => path.replace(/^\/api/, '') },
      '/api/supervisor': { target: 'http://127.0.0.1:8004', changeOrigin: true, rewrite: (path) => path.replace(/^\/api/, '') },
      '/api/enterprise': { target: 'http://127.0.0.1:8005', changeOrigin: true, rewrite: (path) => path.replace(/^\/api/, '') },
      '/api/admin': { target: 'http://127.0.0.1:8007', changeOrigin: true, rewrite: (path) => path.replace(/^\/api/, '') },
      '/api/system': { target: 'http://127.0.0.1:8008', changeOrigin: true, rewrite: (path) => path.replace(/^\/api/, '') },
    },
  },
})
