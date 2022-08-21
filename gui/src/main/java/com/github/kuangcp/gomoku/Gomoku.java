// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   GameFrame.java

package com.github.kuangcp.gomoku;

import javax.swing.*;
import java.awt.*;

public class Gomoku extends JFrame {

    public Gomoku() {
        super("欢乐五子棋");
        ChessBoard chessBoard = new ChessBoard();
        add(chessBoard);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//后台推出
        pack();//自动调整为最佳窗口大小展示
        int screenSizeX = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int screenSizeY = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        int fSizeX = (int) getSize().getWidth();
        int fSizeY = (int) getSize().getHeight();
        setResizable(false);
        setBounds((screenSizeX - fSizeX) / 2, (screenSizeY - fSizeY) / 2, fSizeX, fSizeY);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Gomoku();
    }
}
