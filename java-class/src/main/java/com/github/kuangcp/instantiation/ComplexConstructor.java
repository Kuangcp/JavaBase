package com.github.kuangcp.instantiation;

import lombok.extern.slf4j.Slf4j;

/**
 * @author https://github.com/kuangcp
 * @date 2019-05-15 09:20
 */
@Slf4j
public class ComplexConstructor {

  static abstract class AbstractServer {

    int actualPort;
    static final int DEFAULT_PORT = 8848;

    AbstractServer() {
      actualPort = getPort();

      log.info("port={}", actualPort);
    }

    abstract int getPort();
  }

  public static class Server extends AbstractServer {

    private int port = 8080;

    public Server(int port) {
      this.port = port;
    }

    @Override
    int getPort() {
      return Math.random() > 0.5 ? port : DEFAULT_PORT;
    }
  }
}
