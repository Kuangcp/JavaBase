// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ChessBoard.java

package com.github.kuangcp.gomoku;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;

/**
 * 图标 消息 输入值  选项按钮
 * 整个棋盘
 *
 * @author lenovo
 */
public class ChessBoard extends JPanel
        implements MouseListener, MouseMotionListener {

    private final int SPACE = 44;
    private final int MARGIN = 44;
    private final int WIDTH = 704;
    private final int HEIGHT = 704;
    private final Color BACKGROUND_COLOR;
    private Color chess[][];
    private int gridWithMouseX;
    private int gridWithMouseY;
    private int lastX;
    private int lastY;
    private String first;
    private Color playerColor;
    private Color computerColor;
    private boolean playerPlay;
    private boolean computerPlay;
    private final AI ai;

    public ChessBoard() {
        BACKGROUND_COLOR = Color.orange;
        chess = new Color[15][15];
        gridWithMouseX = -1;
        gridWithMouseY = -1;
        first = "Player";
        ai = new AI();
        setPreferredSize(new Dimension(704, 704));//设置窗体组件大小
        setBackground(BACKGROUND_COLOR);
        addMouseMotionListener(this);
        addMouseListener(this);
        newGame();
    }

    private void newGame() {
        for (int i = 0; i < 15; i++)
            Arrays.fill(chess[i], null);//将颜色数组置空

        repaint();
        //先行者是黑棋
        if (first.equals("Player")) {
            playerPlay = true;
            playerColor = Color.black;
            computerColor = Color.white;
        } else if (first.equals("Computer")) {
            computerPlay = true;
            computerColor = Color.black;
            playerColor = Color.white;
            computerPlayChess();
        }
    }

    private void computerPlayChess() {
        boolean isAIdone = ai.setAI(chess, computerColor);
        int x = ai.getAIX();
        int y = ai.getAIY();
        chess[x][y] = computerColor;
        repaint();
        if (CheckWin.check(chess, x, y, computerColor)) {
            Win(computerColor);
        } else {
            computerPlay = false;
            playerPlay = true;
        }
    }

    private void Win(Color color) {
        JOptionPane.showMessageDialog(this, (new StringBuilder(String.valueOf(color != playerColor ? "电脑" : "你"))).append("赢了!").toString());
        newGame();
    }

    private boolean hasChess(int x, int y) {
        return chess[x][y] == null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        int offset = 4;
        int r = 5;
        int chessR = 20;
        super.paintComponent(g);
        if (gridWithMouseX > 0 && gridWithMouseY > 0 && playerPlay) {
            lastX = gridWithMouseX;
            lastY = gridWithMouseY;
            g.setColor(Color.red);
            g.drawRect(lastX, lastY, 44, 44);
            g.setColor(BACKGROUND_COLOR);
            g.drawLine(lastX + 11, lastY, lastX + 33, lastY);
            g.drawLine(lastX + 11, lastY + 44, lastX + 33, lastY + 44);
            g.drawLine(lastX, lastY + 11, lastX, lastY + 33);
            g.drawLine(lastX + 44, lastY + 11, lastX + 44, lastY + 33);
        } else {
            g.setColor(BACKGROUND_COLOR);
            g.drawRect(lastX, lastY, 44, 44);
        }
        g.setColor(Color.black);
        for (int i = 0; i <= offset; i++)
            g.drawRect(44 - i, 44 - i, 616 + 2 * i, 616 + 2 * i);

        for (int i = 2; i <= 14; i++) {
            g.drawLine(44 + (i - 1) * 44, 44, 44 + (i - 1) * 44, 660);
            g.drawLine(44, 44 + (i - 1) * 44, 660, 44 + (i - 1) * 44);
        }

        for (int i = 3; i <= 11; i += 4) {
            for (int j = 3; j <= 11; j += 4)
                g.fillOval(getXY(i) - r, getXY(j) - r, 2 * r, 2 * r);

        }

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++)
                if (chess[i][j] != null) {
                    g.setColor(chess[i][j]);
                    g.fillOval(getXY(i) - chessR, getXY(j) - chessR, chessR * 2, chessR * 2);
                }

        }

    }

    private int getXY(int xy) {
        return 44 + xy * 44;
    }

    private int getGridXY(int xy) {
        return (xy - 22) / 44;
    }

    public void mouseMoved(MouseEvent e) {
        int x = getGridXY(e.getX());
        int y = getGridXY(e.getY());
        x = x >= 0 ? x : 0;
        x = x <= 14 ? x : 14;
        y = y >= 0 ? y : 0;
        y = y <= 14 ? y : 14;
        if (!hasChess(x, y) || e.getX() >= 682 || e.getX() <= 22 || e.getY() >= 682 || e.getY() <= 22) {
            gridWithMouseX = -1;
            gridWithMouseY = -1;
        } else {
            gridWithMouseX = getXY(x) - 22;
            gridWithMouseY = getXY(y) - 22;
        }
        repaint();
    }

    public void mouseClicked(MouseEvent e) {
        if (playerPlay) {
            int x = getGridXY(e.getX());
            int y = getGridXY(e.getY());
            if (hasChess(x, y))
                chess[x][y] = playerColor;
            repaint();
            if (CheckWin.check(chess, x, y, playerColor)) {
                Win(playerColor);
            } else {
                playerPlay = false;
                computerPlay = true;
                computerPlayChess();
            }
        }
    }

    public void mouseDragged(MouseEvent mouseevent) {
    }

    public void mouseEntered(MouseEvent mouseevent) {
    }

    public void mouseExited(MouseEvent mouseevent) {
    }

    public void mousePressed(MouseEvent mouseevent) {
    }

    public void mouseReleased(MouseEvent mouseevent) {
    }
}
