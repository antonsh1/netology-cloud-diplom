FROM node:18.18
WORKDIR /usr/src/app
#COPY ./frontend/package.json ./
COPY ./frontend .
RUN rm -r node_modules
RUN rm packaje-lock.json
ENV NODE_PATH /usr/src/app/node_modules/
#RUN npm run serve
EXPOSE 8080
#CMD ["npm",  "run", "serve"]
