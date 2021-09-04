# Java中的并发
> [笔记](https://github.com/Kuangcp/Note/blob/master/Java/AdvancedLearning/Concurrency.md)


gradle clean build -x test

docker build -t con .

docker run --cap-add=SYS_PTRACE --name con-test --rm -p 9000:9000 --cpuset-cpus="1" --cpu-quota=5000 --cpu-period=5000  -it con


`--cap-add=SYS_PTRACE`

解决 jmap -heap 1 时报错： Can't attach to the process: ptrace