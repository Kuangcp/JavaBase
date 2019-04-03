# 各种 OOM 场景

> 在容器中运行

创建Java容器, 并设置最大内存上限, 将该文件复制过去, 删除 第一行package

docker run --name jdk8 -it --rm --memory 100M frolvlad/alpine-java:jdk8.202.08-slim

javac file...

java -Xss2M class...
