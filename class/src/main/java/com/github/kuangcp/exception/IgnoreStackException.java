package com.github.kuangcp.exception;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2024-03-31 13:20
 */
public class IgnoreStackException extends RuntimeException {
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
