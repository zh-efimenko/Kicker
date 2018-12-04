#!/bin/bash

##################################
##### ENVIRONMENT VARIABLES ######
##################################
APP_IMAGE_NAME=kicker
APP_SERVICE_NAME=kicker

NETWORK_SERVICES_NAME=kicker-net

POSTGRES_SERVICE_NAME=postgres_kicker
POSTGRES_HOST=postgres
POSTGRES_DB=kicker
POSTGRES_USER=postgres
POSTGRES_PASSWORD=123
OUTER_PORT=5440
VC_POSTGRES_SERVICE_NAME=vc_${POSTGRES_SERVICE_NAME}
VC_POSTGRES_SERVICE_EXIST=`docker ps -aq -f name=${VC_POSTGRES_SERVICE_NAME}`
VOLUME_FOLDER=/var/lib/postgresql/data/pgdata

##################################
####### CLEAN ENVIRONMENT ########
##################################
docker rm -vf ${APP_SERVICE_NAME}
docker rm -vf ${POSTGRES_SERVICE_NAME}
docker rmi -f ${APP_IMAGE_NAME}
docker network rm ${NETWORK_SERVICES_NAME}
# To uncomment if no needed to remove vc
#docker rm -vf vc_${POSTGRES_SERVICE_NAME}

# IF -c THAN EXIT SCRIPT
if [ "$1" = "-c" ];
then
    exit 1
fi

##################################
######### CREATE NETWORK #########
##################################
docker network create ${NETWORK_SERVICES_NAME}

##################################
# CREATE VOLUME CONTAINER FOR DATABASE
##################################
if [ "${VC_POSTGRES_SERVICE_EXIST}" = "" ];
then
    docker create \
        --name ${VC_POSTGRES_SERVICE_NAME} \
        -v ${VOLUME_FOLDER} \
        busybox
fi

##################################
######### RUN POSTGRES ###########
##################################
docker run \
    -d \
    --name ${POSTGRES_SERVICE_NAME} \
    --restart=always \
    --volumes-from ${VC_POSTGRES_SERVICE_NAME} \
    -e PGDATA=${VOLUME_FOLDER} \
    -e POSTGRES_DB=${POSTGRES_DB} \
    -e POSTGRES_USER=${POSTGRES_USER} \
    -e POSTGRES_PASSWORD=${POSTGRES_PASSWORD} \
    -p ${OUTER_PORT}:5432 \
    postgres:9.6.11

##################################
### CREATE ALIAS FOR HOSTNAME ####
##################################
docker network connect --alias ${POSTGRES_HOST} \
                                ${NETWORK_SERVICES_NAME} \
                                ${POSTGRES_SERVICE_NAME}

##################################
############ RUN APP #############
##################################
docker run \
    -i \
    --name ${APP_SERVICE_NAME} \
    --network ${NETWORK_SERVICES_NAME} \
    -e POSTGRES_HOST=${POSTGRES_HOST} \
    -e POSTGRES_DB=${POSTGRES_DB} \
    -e POSTGRES_USER=${POSTGRES_USER} \
    -e POSTGRES_PASSWORD=${POSTGRES_PASSWORD} \
    -p 8080:8080 \
    ${APP_IMAGE_NAME}