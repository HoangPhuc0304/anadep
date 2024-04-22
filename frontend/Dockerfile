FROM node:lts-alpine as build
WORKDIR /app
COPY package.json .
RUN npm install
COPY . .
RUN npm run build

FROM nginx:latest as prod
COPY --from=build /app/build /usr/share/nginx/html
COPY --from=build /app/nginx.conf /etc/nginx/nginx.conf
EXPOSE 80/tcp
LABEL version=1.0.0
CMD ["/usr/sbin/nginx", "-g", "daemon off;"]