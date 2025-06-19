package com.github.kuangcp.caculator;

public class CalculatorException extends RuntimeException {
    public CalculatorException(String message) {
        super(message);
    }

    public CalculatorException(String message, Throwable cause) {
        super(message, cause);
    }
} 