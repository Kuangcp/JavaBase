# Tank
> like `Tank 1990`

1. build: `gradle clean tank`
2. extract: `unzip out/build/distributions/gui.zip`
3. run: `./gui/bin/gui`

 -javaagent:/path/to/quasar-core-0.7.4-jdk8.jar

direct run: `ge tank -x test && cp out/build/distributions/gui.tar . && tar xf gui.tar && ./gui/bin/gui`

## TODO
1. 碰撞监测
1. 对方运动算法
1. 网络联机
1. 持久化存储