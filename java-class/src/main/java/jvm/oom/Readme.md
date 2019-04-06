# 各种 OOM 场景
- [OutOfMemoryError系列（1）: Java heap space](https://blog.csdn.net/renfufei/article/details/76350794)
- [OutOfMemoryError系列（2）: GC overhead limit exceeded](https://blog.csdn.net/renfufei/article/details/77585294)
- [OutOfMemoryError系列（3）: Permgen space](https://blog.csdn.net/renfufei/article/details/77994177#commentBox)
- [OutOfMemoryError系列（4）: Metaspace](https://blog.csdn.net/renfufei/article/details/78061354)

> 在容器中运行

创建Java容器, 并设置最大内存上限, 将该文件复制过去
删除 第一行 package, 如果文件依赖了三方jar, 就有点麻烦 要把jar复制进去, 运行时classpath中加上jar路径

docker run --name jdk8 -it --rm --memory 100M frolvlad/alpine-java:jdk8.202.08-slim

进入容器

javac A.java

java A

疑问: 好几种情况并没有出现OOM而是直接被Killed了, 要思考Docker内存限制的实现方式了
