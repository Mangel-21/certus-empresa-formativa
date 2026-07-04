FROM node:22.22.3-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install --legacy-peer-deps
COPY . .
RUN npm run build --configuration=production

FROM nginx:alpine
COPY --from=build /app/dist/webapp/browser /usr/share/nginx/html

RUN echo '#!/bin/sh' > /docker-entrypoint.d/40-inject-env.sh && \
    echo 'echo "(function(window) { window.__env = window.__env || {}; window.__env.GOOGLE_CLIENT_ID = \"${GOOGLE_CLIENT_ID}\"; })(this);" > /usr/share/nginx/html/env.js' >> /docker-entrypoint.d/40-inject-env.sh && \
    chmod +x /docker-entrypoint.d/40-inject-env.sh

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
