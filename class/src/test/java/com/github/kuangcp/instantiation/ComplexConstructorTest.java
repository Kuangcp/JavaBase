package com.github.kuangcp.instantiation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import com.github.kuangcp.instantiation.ComplexConstructor.Server;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp
 * @date 2019-05-15 09:21
 */
@Slf4j
public class ComplexConstructorTest {

  @Test
  public void test() {
    Server server = new Server(3306);

    // TODO 解释
    assertThat(server.actualPort == 0 || server.actualPort == Server.DEFAULT_PORT,
        equalTo(true));
  }
}
