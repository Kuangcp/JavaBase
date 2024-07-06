package com.github.kuangcp.instantiation;

import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a>
 */
@Slf4j
class ComplexConstructor {

    static class Server extends AbstractServer {

        private int port = 8080;

        Server(int port) {
            this.port = port;
        }

        @Override
        int getPort() {
            return port;
        }
    }
}

@Slf4j
abstract class AbstractServer {

    int actualPort;

    AbstractServer() {
        actualPort = getPort();
        log.info("actualPort={}", actualPort);
    }

    abstract int getPort();
}