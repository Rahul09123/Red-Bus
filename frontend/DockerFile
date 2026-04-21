FROM node:20-alpine

WORKDIR /app

# Paket dosyaları
COPY package*.json ./
RUN npm install

# Kaynak kodlar
COPY . .

# Vite dev server portu
EXPOSE 5173

# Frontend'i DEV modunda çalıştır
CMD ["npm", "run", "dev", "--", "--host"]
