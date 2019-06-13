#!/bin/bash

docker ps -a > images-list.txt

CONTAINER_ID=$(sed -e '\/emartej\/tictactoe/ !d' images-list.txt | cut -d' ' -f1 )

docker inspect $CONTAINER_ID > image-details.txt

CONTAINER_IP=$(sed -e '/"IPAddress"/ !d' image-details.txt | cut -d':' -f2 | cut -d'"' -f2 | cut -d$'\n' -f1)

echo $CONTAINER_IP > container-ip.txt