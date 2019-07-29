package com.github.kuangcp.customer.dao;

import com.github.kuangcp.base.TestStarter;
import com.github.kuangcp.mock.map.MockValue;
import com.github.kuangcp.customer.domain.Customer;
import java.util.List;
import java.util.stream.LongStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class CustomerDaoTest extends TestStarter {

  @Autowired
  private CustomerDao customerDao;

  @Test
  public void testQuery() {
    customerDao.insert(Customer.builder().name("myth").build());
    List<Customer> customers = customerDao.queryByName("myth");

    customers.forEach(item -> log.info("item={}", item));
  }
  
  @Test
  public void testBulkInsert(){
    LongStream.rangeClosed(1, 1000)
        .mapToObj(i -> Customer.builder().id(i)
            .id(i)
            .nation(MockValue.mock(Integer.class))
            .build())
        .forEach(customerDao::insert);
  }
}