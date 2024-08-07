package com.github.kuangcp.instantiation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import com.github.kuangcp.instantiation.ComplexConstructor.Server;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a>
 * @date 2019-05-15 09:21
 */
@Slf4j
public class ComplexConstructorTest {

  @Test
  public void testInitField() {
    Server server = new Server(3306);

    // TODO 永远为 0
    assertThat(server.actualPort, equalTo(0));
  }
}
