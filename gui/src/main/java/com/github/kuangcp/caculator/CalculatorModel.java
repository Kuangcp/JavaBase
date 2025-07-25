package com.github.kuangcp.caculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.Getter;

public class CalculatorModel {
    private static final int SCALE = 10;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    @Getter
    private BigDecimal currentValue = BigDecimal.ZERO;
    private BigDecimal storedValue = null;
    private Operation currentOperation = null;
    private boolean newNumber = true;
    private boolean hasDecimalPoint = false;

    public void setNumber(String num) {
        if (newNumber) {
            currentValue = new BigDecimal(num);
            newNumber = false;
        } else {
            String current = currentValue.toString();
            if (hasDecimalPoint) {
                current = current + num;
            } else {
                current = current + num;
            }
            currentValue = new BigDecimal(current);
        }
    }

    public void setOperation(Operation op) {
        if (storedValue == null) {
            storedValue = currentValue;
        } else if (currentOperation != null) {
            storedValue = calculate();
        }
        currentOperation = op;
        newNumber = true;
        hasDecimalPoint = false;
    }

    public void addDecimalPoint() {
        if (!hasDecimalPoint) {
            hasDecimalPoint = true;
            if (newNumber) {
                currentValue = BigDecimal.ZERO;
                newNumber = false;
            }
        }
    }

    public void negate() {
        currentValue = currentValue.negate();
    }

    public void clear() {
        currentValue = BigDecimal.ZERO;
        storedValue = null;
        currentOperation = null;
        newNumber = true;
        hasDecimalPoint = false;
    }

    public BigDecimal calculate() {
        if (currentOperation == null || storedValue == null) {
            return currentValue;
        }

        try {
            BigDecimal result = currentOperation.apply(storedValue, currentValue);
            result = result.setScale(SCALE, ROUNDING_MODE);
            currentValue = result;
            storedValue = null;
            currentOperation = null;
            newNumber = true;
            hasDecimalPoint = false;
            return result;
        } catch (ArithmeticException e) {
            throw new CalculatorException("计算错误: " + e.getMessage());
        }
    }

    public void backspace() {
        if (newNumber) {
            return;
        }
        String current = currentValue.toString();
        if (current.length() <= 1) {
            currentValue = BigDecimal.ZERO;
            newNumber = true;
        } else {
            current = current.substring(0, current.length() - 1);
            if (current.endsWith(".")) {
                current = current.substring(0, current.length() - 1);
                hasDecimalPoint = false;
            }
            currentValue = new BigDecimal(current);
        }
    }
} 