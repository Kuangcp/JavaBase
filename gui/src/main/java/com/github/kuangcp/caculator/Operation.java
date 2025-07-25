package com.github.kuangcp.caculator;

import java.math.BigDecimal;

public enum Operation {
    ADD("+") {
        @Override
        BigDecimal apply(BigDecimal x, BigDecimal y) {
            return x.add(y);
        }
    },
    SUBTRACT("-") {
        @Override
        BigDecimal apply(BigDecimal x, BigDecimal y) {
            return x.subtract(y);
        }
    },
    MULTIPLY("*") {
        @Override
        BigDecimal apply(BigDecimal x, BigDecimal y) {
            return x.multiply(y);
        }
    },
    DIVIDE("/") {
        @Override
        BigDecimal apply(BigDecimal x, BigDecimal y) {
            if (y.compareTo(BigDecimal.ZERO) == 0) {
                throw new CalculatorException("除数不能为零");
            }
            return x.divide(y, 10, BigDecimal.ROUND_HALF_UP);
        }
    };
    
    private final String symbol;
    
    Operation(String symbol) {
        this.symbol = symbol;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    abstract BigDecimal apply(BigDecimal x, BigDecimal y);
    
    public static Operation fromSymbol(String symbol) {
        for (Operation op : values()) {
            if (op.symbol.equals(symbol)) {
                return op;
            }
        }
        throw new CalculatorException("未知的运算符: " + symbol);
    }
} 