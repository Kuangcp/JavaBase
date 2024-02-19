package com.github.kuangcp.distri;

import org.junit.Test;

/**
 *
 * @author Kuangcp
 * 2024-02-19 14:20
 */
public class SnowflakeTest {

    @Test
    public void test() throws Exception {
        Snowflake idWorker = new Snowflake(0, 0);
        for (int i = 0; i < 1000; i++) {
            long id = idWorker.nextId();
//      System.out.println(Long.toBinaryString(id));
            System.out.println(id);
        }
    }
}