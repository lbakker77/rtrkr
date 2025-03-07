
FROM node:22-alpine as build
RUN npm install -g @angular/cli
WORKDIR /app
COPY package*.json /app/
RUN npm install
COPY . /app
ARG configuration=production
RUN ng build --configuration $configuration --output-path dist/out

FROM nginx:1.27
COPY --from=build /app/dist/out/browser /usr/share/nginx/html
