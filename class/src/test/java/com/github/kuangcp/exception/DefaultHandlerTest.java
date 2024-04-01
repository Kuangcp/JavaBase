package com.github.kuangcp.exception;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.Statement;

/**
 * https://www.baeldung.com/java-global-exception-handler
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2024-03-31 17:04
 */
@Slf4j
public class DefaultHandlerTest {

    /**
     * @see ParentRunner#runLeaf Junit 中catch了异常，所以不会进入到默认逻辑
     */
    @Test
    public void testDefaultHandler() throws Exception {
        setUp();
        hitDefault();
    }

    public static void main(String[] args) {
        setUp();
        notHitDefault();
        hitDefault2();
//        hitDefault();
    }

    private static void setUp() {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            log.error("DEFAULT", e);
            System.out.println(e.getMessage().length());
        });
    }

    private static void hitDefault() {
        System.out.println(">>> Thread " + Thread.currentThread().getName());
        System.out.println(1 / 0);
        System.out.println("handle");
    }

    private static void hitDefault2() {
        System.out.println(">>> hitDefault2 " + Thread.currentThread().getName());
        try {
            System.out.println(1 / 0);
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }

        System.out.println("handle");
    }


    private static void notHitDefault() {
        System.out.println(">>> Thread " + Thread.currentThread().getName());
        try {
            System.out.println(1 / 0);
        } catch (Exception e) {
            // 异常已经被此处声明的 handler 捕获，不会进入上述声明的 默认handler
            log.error("", e);
        }
        System.out.println("handle");
    }
}
