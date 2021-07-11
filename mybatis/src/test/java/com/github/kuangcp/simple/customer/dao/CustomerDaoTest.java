package com.github.kuangcp.simple.customer.dao;

import com.github.kuangcp.base.TestStarter;
import com.github.kuangcp.sharding.manual.AuthUtil;
import com.github.kuangcp.simple.customer.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.LongStream;

@Slf4j
public class CustomerDaoTest extends TestStarter {

    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private AuthUtil authUtil;

    private void login(Long orgId){
        authUtil.clearAuth();
        authUtil.completeAuth(orgId);
    }
    @Test
    public void testQuery() {
        login(2L);
        customerDao.insert(Customer.builder().id(1L).name("myth").build());
        List<Customer> customers = customerDao.queryByName("myth");

        customers.forEach(item -> log.info("item={}", item));
    }

    @Test
    public void testBulkInsert() {

        LongStream.rangeClosed(1, 1000)
                .mapToObj(i -> Customer.builder().id(i)
                        .id(i)
                        .nation(3)
                        .build())
                .forEach(customerDao::insert);
    }
}