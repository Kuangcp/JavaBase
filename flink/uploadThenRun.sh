result=$(./upload.sh)

temp=${result#*flink-web-upload/}
jarId=${temp%%\"*}

echo "start run: " $jarId
./run.sh $jarId

