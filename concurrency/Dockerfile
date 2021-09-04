FROM mythkuang/jdk-alpine-cst:8.181

WORKDIR /app

COPY ./out/build/distributions/concurrency.tar /app

RUN tar -xvf  concurrency.tar && \
    sed -i 's/DEFAULT_JVM_OPTS=""/DEFAULT_JVM_OPTS="-server \
    -Xms200m \
    -Xmx200m \
    -Xss256k\
    -verbose:gc\
    -XX:+PrintGCDetails\
    -XX:+PrintGCDateStamps\
    -Xloggc:\/app\/gc.log\
    -XX:ErrorFile=\/app\/hs_err_pid%p.log"/g' /app/concurrency/bin/concurrency

CMD ["/app/concurrency/bin/concurrency"]
