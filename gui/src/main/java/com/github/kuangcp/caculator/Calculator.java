package com.github.kuangcp.caculator;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

@Slf4j
public class Calculator extends JFrame {
    private final CalculatorModel model;
    private final JTextField display;
    private final JTextField inputCache;

    public Calculator() {
        model = new CalculatorModel();
        display = new JTextField("0");
        inputCache = new JTextField("");

        try {
            initializeFrame();
            initializeDisplay();
            initializeButtons();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void initializeFrame() {
        setTitle("计算器");
        setSize(new Dimension(400, 300));
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
    }

    private void initializeDisplay() {
        display.setEnabled(false);
        display.setEditable(false);
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setBounds(33, 19, 310, 34);

        inputCache.setEnabled(false);
        inputCache.setEditable(false);
        inputCache.setHorizontalAlignment(SwingConstants.RIGHT);
        inputCache.setBounds(27, 19, 310, 8);

        getContentPane().add(display);
        getContentPane().add(inputCache);
    }

    private void initializeButtons() {
        // 数字按钮
        ActionListener numberListener = e -> {
            try {
                String number = e.getActionCommand();
                model.setNumber(number);
                updateDisplay();
            } catch (Exception ex) {
                log.error("数字输入错误", ex);
                display.setText("错误");
            }
        };

        int startX = 30;
        int startY = 170;
        int deltaW = 70;
        int deltaH = 50;

        // 创建数字按钮 1-9
        for (int i = 1; i <= 9; i++) {
            int x = startX + ((i - 1) % 3) * deltaW;
            int y = startY - ((i - 1) / 3) * deltaH;
            getContentPane().add(CalculatorButtonFactory.createNumberButton(
                    String.valueOf(i), x, y, numberListener));
        }

        // 创建数字0按钮
        getContentPane().add(CalculatorButtonFactory.createNumberButton(
                "0", startX, startY + deltaH, numberListener));

        // 运算符按钮
        ActionListener operatorListener = e -> {
            try {
                Operation op = Operation.fromSymbol(e.getActionCommand());
                model.setOperation(op);
                updateDisplay();
            } catch (Exception ex) {
                log.error("运算符输入错误", ex);
                display.setText("错误");
            }
        };

        // 添加运算符按钮
        getContentPane().add(CalculatorButtonFactory.createOperationButton(
                Operation.ADD, 234, 70, operatorListener));
        getContentPane().add(CalculatorButtonFactory.createOperationButton(
                Operation.SUBTRACT, 234, 120, operatorListener));
        getContentPane().add(CalculatorButtonFactory.createOperationButton(
                Operation.MULTIPLY, 234, 172, operatorListener));
        getContentPane().add(CalculatorButtonFactory.createOperationButton(
                Operation.DIVIDE, 234, 222, operatorListener));

        // 特殊按钮
        getContentPane().add(CalculatorButtonFactory.createSpecialButton(
                "=", 298, 222, e -> {
                    try {
                        model.calculate();
                        updateDisplay();
                    } catch (Exception ex) {
                        log.error("计算错误", ex);
                        display.setText(ex.getMessage());
                    }
                }));

        getContentPane().add(CalculatorButtonFactory.createSpecialButton(
                "C", 298, 121, e -> {
                    model.clear();
                    updateDisplay();
                }));

        getContentPane().add(CalculatorButtonFactory.createSpecialButton(
                ".", 167, 222, e -> {
                    model.addDecimalPoint();
                    updateDisplay();
                }));

        getContentPane().add(CalculatorButtonFactory.createSpecialButton(
                "+/-", 101, 222, e -> {
                    model.negate();
                    updateDisplay();
                }));

        getContentPane().add(CalculatorButtonFactory.createSpecialButton(
                "←", 298, 172, e -> {
                    model.backspace();
                    updateDisplay();
                }));
    }

    private void updateDisplay() {
        display.setText(model.getCurrentValue().stripTrailingZeros().toPlainString());
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.setLocation(200, 150);
        calculator.setVisible(true);
    }
}

