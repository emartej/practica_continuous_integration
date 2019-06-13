#!/bin/sh       
#tag=$(grep '<version>' $2 | sed 's/<version>[^-]*-\(.*\)<\/version>/\1/')
#sed -i "s/<version>.*<\/version>/<version>$1-${tag}<\/version>/" $2




sed '/<parent>/,/<\/dependencies>/d;/<version>/!d;s/ *<\/\> *//1' pom.xml > tag.txt
myVar = $(cat tag.txt)
echo $myVar


#sed -i -e 's/^[ \t]*//; s/[ \t]*$//; /^$/d' tag.txt > tag2.txt
#cat tag2.txt

#echo $tag
