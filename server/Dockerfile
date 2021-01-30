#### Build environment ####
FROM node:15-alpine as build

WORKDIR /app

COPY package*.json ./

RUN npm install

RUN npm install tsc -g

COPY . .

RUN npm run build

#### Production environment ####
FROM node:15-alpine 

WORKDIR /usr/src/server

# Copie des fichiers nécessaires à l'execution du serveur
COPY --from=build /app/dist /usr/src/server
COPY --from=build /app/node_modules /usr/src/server/node_modules

# Variables d'environement pour le conteneur
ENV MONGO_URI=mongodb+srv://admin:admin@cluster0.tdioz.mongodb.net/prod?retryWrites=true&w=majority
ENV PORT=5000
ENV HASH_SECRET=mysecret
ENV JWT_SECRET=jwtsecret
ENV JWT_VERSION=0
ENV TZ=Morocc

EXPOSE 5000

CMD ["node","app"]