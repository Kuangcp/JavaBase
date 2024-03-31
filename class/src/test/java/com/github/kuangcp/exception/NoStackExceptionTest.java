package com.github.kuangcp.exception;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2024-03-31 13:21
 */
@Slf4j
public class NoStackExceptionTest {

    private void a() {
        throw new IgnoreStackException();
    }

    private void b() {
        throw new RuntimeException();
    }

    @Test
    public void testFillStack() throws Exception {
        try {
            a();
        } catch (Exception e) {
            log.error("", e);
        }
        try {
            b();
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
