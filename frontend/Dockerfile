FROM node:lts-alpine as build
WORKDIR /app
COPY package.json .
RUN npm install
COPY . .

ARG REACT_APP_BASE_URL
ARG REACT_APP_AUTH_CLIENT_ID
ARG REACT_APP_ENABLE_ANADEP_DB
ARG DEV_TOOLS

RUN REACT_APP_BASE_URL=${REACT_APP_BASE_URL} \
    REACT_APP_AUTH_CLIENT_ID=${REACT_APP_AUTH_CLIENT_ID} \
    REACT_APP_ENABLE_ANADEP_DB=${REACT_APP_ENABLE_ANADEP_DB} \
    DEV_TOOLS=${DEV_TOOLS} \
    npm run build

FROM nginx:latest as prod
COPY --from=build /app/build /usr/share/nginx/html
COPY --from=build /app/nginx.conf /etc/nginx/nginx.conf
EXPOSE 80/tcp
LABEL version=1.0.0
CMD ["/usr/sbin/nginx", "-g", "daemon off;"]