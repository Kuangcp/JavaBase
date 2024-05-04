# 各种 OOM 场景

- [OutOfMemoryError系列（1）: Java heap space](https://blog.csdn.net/renfufei/article/details/76350794)
- [OutOfMemoryError系列（2）: GC overhead limit exceeded](https://blog.csdn.net/renfufei/article/details/77585294)
- [OutOfMemoryError系列（3）: Permgen space](https://blog.csdn.net/renfufei/article/details/77994177#commentBox)
- [OutOfMemoryError系列（4）: Metaspace](https://blog.csdn.net/renfufei/article/details/78061354)

## 在容器中运行

创建Java容器, 并设置最大内存上限, 将该文件复制过去

删除 第一行 package, 如果文件依赖了三方jar, 就有点麻烦 要把jar复制进去, 运行时classpath中加上jar路径

- java8 `docker run --name jdk8 -it --rm --memory 100M frolvlad/alpine-java:jdk8.202.08-slim`
- java7 `docker run --name jdk7 -it --rm --memory 100M  mythkuang/java:7u121-openjdk-alpine`

进入容器

javac A.java

java A

好几种情况并没有出现OOM,而是直接被Killed了

- 这是因为 Linux 的 OOM Killer [Linux OOM killer](https://segmentfault.com/a/1190000008268803)
- 执行 dmesg 可以看到类似于 `Out of memory: Kill process 10375 (java) score 59 or sacrifice child`

关闭 oom-killer机制后，发现还是不会出现OOM，容器就被回收了

是因为JDK无法感知容器的资源限制，到8U191/10b34 及以上版本才支持

***