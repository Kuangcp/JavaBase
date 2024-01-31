package com.github.kuangcp.volatiles;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author kuangcp
 * 2024-01-31 17:40
 */
public class ThisEscapeTest {

    @Test
    public void testOnce() throws Exception {
        ThisEscape es = new ThisEscape();
        System.out.println(es.i + " " + es.j);
        TimeUnit.SECONDS.sleep(1);
        System.out.println(es.i + " " + es.j);
    }

    @Test
    public void testMain() throws Exception {
        for (int i = 0; i < 90000; i++) {
            new ThisEscape();
        }
    }
}
