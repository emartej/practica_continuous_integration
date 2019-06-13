#!/bin/bash

SNAPSHOT="-SNAPSHOT"
if [[ $2 == *SNAPSHOT ]]
then
    NEW_VER=$2
else
    NEW_VER="$2$SNAPSHOT"
fi

sed -i 's/'"$1"'/'"$NEW_VER"'/' ./pom.xml
