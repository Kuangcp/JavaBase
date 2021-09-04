# Java中的并发
> [笔记](https://github.com/Kuangcp/Note/blob/master/Java/AdvancedLearning/Concurrency.md)

应尽量避免线程的阻塞和等待，因为这是在浪费CPU

gradle clean build -x test
docker build -t con .
docker run --rm -p 9000:9000 --cpuset-cpus="1" --cpu-quota=5000 --cpu-period=5000  -it con