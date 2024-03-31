package com.github.kuangcp.exception;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2024-03-31 14:45
 */
@Slf4j
public class ExceptionThrowTest {

    @Test
    public void testThrow() throws Exception {
        try {
            int a = 1, b = 1;
            if (a == b) {
                throw new RuntimeException();
            }
            System.out.println(a + b);
        } catch (IndexOutOfBoundsException e) {
            throw e;
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }
    }

    @Test
    public void testLoss() throws Exception {
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                System.out.println(5/i);
            }
        }).start();

        TimeUnit.SECONDS.sleep(3);
    }
}


