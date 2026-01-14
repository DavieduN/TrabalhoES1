import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    host: true,
    port: 5173,
    watch: {
      usePolling: true,
    },
    hmr: {
      clientPort: 5174, 
    },
    proxy: {
      '/api': {
        target: 'http://app:8080/aluguel',
        changeOrigin: true,
        secure: false,
      },
      '/enderecos': {
        target: 'http://app:8080/endereco',
        changeOrigin: true,
        secure: false,
      },
      '/endereco': {
        target: 'http://app:8080',
        changeOrigin: true,
        secure: false,
      }
    }
  },
})