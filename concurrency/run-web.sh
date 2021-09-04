gradle clean build -x test

tar xf ./out/build/distributions/concurrency.tar -C ./out/build/distributions/

sed -i 's/DEFAULT_JVM_OPTS=""/DEFAULT_JVM_OPTS="-server \
    -Xms200m \
    -Xmx200m \
    -Xss256k\
    -verbose:gc\
    -XX:+PrintGCDetails\
    -XX:+PrintGCDateStamps\
    -Xloggc:\/app\/gc.log\
    -XX:ErrorFile=\/app\/hs_err_pid%p.log"/g' ./out/build/distributions/concurrency/bin/concurrency

docker build -t con .

# docker run --cap-add=SYS_PTRACE  --name con-test --rm -p 9000:9000 --cpuset-cpus="1" --cpu-quota=5000 --cpu-period=5000  -it con
# docker run --cap-add=SYS_PTRACE --name con-test --rm -p 9000:9000 -it con
