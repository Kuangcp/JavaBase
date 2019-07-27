package com.github.kuangcp.user.dao;

import com.github.kuangcp.base.BaseDaoTest;
import com.github.kuangcp.mock.map.MockValue;
import com.github.kuangcp.user.domain.User;
import java.util.List;
import java.util.stream.LongStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class UserDaoTestTest extends BaseDaoTest {

  @Autowired
  private UserDao userDao;

  @Test
  public void testQuery() {
    userDao.insert(User.builder().name("myth").build());
    List<User> users = userDao.queryByName("myth");

    users.forEach(item -> log.info("item={}", item));
  }
  
  @Test
  public void testBulkInsert(){
    LongStream.rangeClosed(1, 1000)
        .mapToObj(i -> User.builder().id(i)
            .id(i)
            .nation(MockValue.mock(Integer.class))
            .build())
        .forEach(userDao::insert);
  }
}