package com.github.kuangcp.wrapper;

import com.github.kuangcp.wrapper.domain.Book;
import com.github.kuangcp.wrapper.domain.Topics;
import com.github.kuangcp.wrapper.domain.User;
import java.util.Arrays;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-11-23 21:58
 */
@Slf4j
public class KafkaProducerWrapperTest {


  @Test
  public void testSendPlainText() {
    KafkaProducerWrapper.sendPlainText(Topics.HI, "test send message");
  }

  @Test
  public void testSendMessage() {
    Book book = Book.builder().name("test").type("math").build();
    User user = User.builder().name("one").nickName("two")
        .phones(Arrays.asList("11111", "22222"))
        .books(Collections.singletonList(book))
        .build();
    KafkaProducerWrapper.send(Topics.USER_LOGIN, user);
  }
}
