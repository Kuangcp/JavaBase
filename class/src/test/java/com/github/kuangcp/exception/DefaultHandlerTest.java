package com.github.kuangcp.exception;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * https://www.baeldung.com/java-global-exception-handler
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2024-03-31 17:04
 */
@Slf4j
public class DefaultHandlerTest {

    @Test
    public void testDefaultHandler() throws Exception {
        // TODO not work ?
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            log.error("", e);
            System.out.println(e.getMessage().length());
        });
        System.out.println(1 / 0);
        System.out.println("handle");
    }

    public static void main(String[] args) {
        try {
            hitDefault();
        } catch (Exception e) {
            log.error("", e);
        }

        try {
            notHitDefault();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private static void hitDefault() {
        System.out.println(Thread.currentThread().getName());
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            log.error("", e);
            System.out.println(e.getMessage().length());
        });
        System.out.println(1 / 0);
        System.out.println("handle");
    }

    private static void notHitDefault() {
        System.out.println(Thread.currentThread().getName());
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            log.error("", e);
            System.out.println(e.getMessage().length());
        });
        try {
            System.out.println(1 / 0);
        } catch (Exception e) {
            log.error("", e);
        }
        System.out.println("handle");
    }
}
