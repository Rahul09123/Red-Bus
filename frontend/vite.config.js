import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: true, // docker dışından erişim için
    port: 5173,
    proxy: {
      "/api": {
        target: "http://api-gateway:8080",
        changeOrigin: true,
        secure: false,
      },
    },
  },
});
