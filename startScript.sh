#!/bin/bash
# for-schleife

if (($# != 2))
then
	echo "es müssen 2 argumente angegeben werden!"
	exit 0
fi

width=$1
height=$2

displayWidth=1900
displayHeight=1000

currentX=0
currentY=0

while ((currentX < displayWidth))
do
	currentY=0

	while ((currentY < displayHeight))
	do
		java.exe -jar desktop-1.0.jar $width";"$height $currentX";"$currentY &
		currentY=$((currentY+height))
	done


	currentX=$((currentX+width))
done
