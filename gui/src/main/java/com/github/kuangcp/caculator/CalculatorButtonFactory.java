package com.github.kuangcp.caculator;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class CalculatorButtonFactory {
    private static final Font STANDARD_FONT = new Font("Dialog", Font.PLAIN, 16);
    private static final Dimension BUTTON_SIZE = new Dimension(47, 38);
    
    public static JButton createNumberButton(String number, int x, int y, ActionListener listener) {
        JButton button = new JButton(number);
        configureButton(button, x, y);
        button.addActionListener(listener);
        return button;
    }
    
    public static JButton createOperationButton(Operation op, int x, int y, ActionListener listener) {
        JButton button = new JButton(op.getSymbol());
        configureButton(button, x, y);
        button.addActionListener(listener);
        return button;
    }
    
    public static JButton createSpecialButton(String text, int x, int y, ActionListener listener) {
        JButton button = new JButton(text);
        configureButton(button, x, y);
        button.addActionListener(listener);
        return button;
    }
    
    private static void configureButton(JButton button, int x, int y) {
        button.setFont(STANDARD_FONT);
        button.setPreferredSize(BUTTON_SIZE);
        button.setBounds(new Rectangle(x, y, BUTTON_SIZE.width, BUTTON_SIZE.height));
    }
} 