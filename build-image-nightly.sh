#!/bin/bash
YYYYMMDD=$(date +%Y%m%d)
PROJ_VER=$(sed '/<parent>/,/<\/dependencies>/d;/<version>/!d;s/ *<\/\> *//1' pom.xml)
PROJ_VER_2=${PROJ_VER:10}
PROJ_VER_3=$(sed 's/.\{19\}$//' <<< "$PROJ_VER_2")
IM_NAME=$PROJ_VER_3."nightly."$YYYYMMDD
echo $IM_NAME > container-name.txt

docker build --build-arg GIT_COMMIT=$(git rev-parse HEAD) --build-arg COMMIT_DATE=$(git log -1 --format=%cd --date=format:%Y-%m-%dT%H:%M:%S) -t emartej/tictactoe:$IM_NAME .
