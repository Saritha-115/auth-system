# ======================
# BUILD STAGE
# ======================
FROM node:18-alpine AS build

WORKDIR /app

COPY package.json package-lock.json ./

RUN npm install

COPY . .

# VITE environment variable (correct for your stack)
ARG VITE_API_URL=http://localhost:8080
ENV VITE_API_URL=$VITE_API_URL

RUN npm run build

# ======================
# PRODUCTION STAGE
# ======================
FROM nginx:alpine

# Vite output is /dist (NOT /build)
COPY --from=build /app/dist /usr/share/nginx/html

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]