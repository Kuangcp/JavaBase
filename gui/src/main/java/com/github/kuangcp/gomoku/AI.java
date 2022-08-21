// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AI.java

package com.github.kuangcp.gomoku;

import java.awt.*;
import java.util.Random;

public class AI {

    private Color chess[][];
    private Color myColor;
    private Color antiColor;
    private int bestX;
    private int bestY;
    private int effectiveX1;
    private int effectiveY1;
    private int effectiveX2;
    private int effectiveY2;
    private Color colorArr[];
    private final Random rand;

    public AI() {
        colorArr = (new Color[]{
                Color.black, Color.white
        });
        rand = new Random();
    }

    public boolean setAI(Color chess[][], Color myColor) {
        this.chess = chess;
        this.myColor = myColor;
        antiColor = myColor != Color.black ? Color.black : Color.white;
        findEffectiveArea();
        findBestPosition();
        return true;
    }

    public int getAIX() {
        return bestX;
    }

    public int getAIY() {
        return bestY;
    }

    private void findEffectiveArea() {
        for (int i = 0; i <= 14; i++) {
            for (int j = 0; j <= 14; j++)
                if (chess[i][j] != null) {
                    effectiveX1 = i;
                    effectiveY1 = j;
                    effectiveX2 = i;
                    effectiveY2 = j;
                }

        }

        for (int i = 0; i <= 14; i++) {
            for (int j = 0; j <= 14; j++)
                if (chess[i][j] != null) {
                    effectiveX1 = Math.min(effectiveX1, i);
                    effectiveY1 = Math.min(effectiveY1, j);
                    effectiveX2 = Math.max(effectiveX2, i);
                    effectiveY2 = Math.max(effectiveY2, j);
                }

        }

        int offset = 2;
        effectiveX1 = effectiveX1 - offset < 0 ? effectiveX1 : effectiveX1 - offset;
        effectiveY1 = effectiveY1 - offset < 0 ? effectiveY1 : effectiveY1 - offset;
        effectiveX2 = effectiveX2 + offset > 14 ? effectiveX2 : effectiveX2 + offset;
        effectiveY2 = effectiveY2 + offset > 14 ? effectiveY2 : effectiveY2 + offset;
    }

    private void findBestPosition() {
        int tempScore = 0;
        bestX = effectiveX1;
        bestY = effectiveY1;
        for (int x = effectiveX1; x <= effectiveX2; x++) {
            for (int y = effectiveY1; y <= effectiveY2; y++)
                if (chess[x][y] == null && tempScore < getScore(x, y)) {
                    bestX = x;
                    bestY = y;
                    tempScore = getScore(x, y);
                }

        }

        if (tempScore == 0) {
            bestX = effectiveX1 + rand.nextInt((effectiveX2 + 1) - effectiveX1);
            bestY = effectiveY1 + rand.nextInt((effectiveY2 + 1) - effectiveY1);
        }
    }

    private int contains(int x, int y, Color color) {
        if (x < 0 || x > 14 || y < 0 || y > 14)
            return -1;
        if (chess[x][y] == color)
            return 1;
        return chess[x][y] != null ? -1 : 0;
    }

    private int getScore(int x, int y) {
        int tempX = x;
        int tempY = y;
        int count = 0;
        int block = 0;
        int totalScore = 0;
        Color acolor[];
        int j = (acolor = colorArr).length;
        for (int i = 0; i < j; i++) {
            Color color = acolor[i];
            x = tempX;
            y = tempY;
            count = 0;
            block = 0;
            while (contains(x - 1, y, color) == 1) {
                x--;
                count++;
            }
            if (contains(x - 1, y, color) == -1)
                block++;
            x = tempX;
            for (y = tempY; contains(x + 1, y, color) == 1; ) {
                x++;
                count++;
            }

            if (contains(x + 1, y, color) == -1)
                block++;
            count++;
            totalScore += countToScore(count, block, color);
            x = tempX;
            y = tempY;
            count = 0;
            block = 0;
            while (contains(x, y - 1, color) == 1) {
                y--;
                count++;
            }
            if (contains(x, y - 1, color) == -1)
                block++;
            x = tempX;
            for (y = tempY; contains(x, y + 1, color) == 1; ) {
                y++;
                count++;
            }

            if (contains(x, y + 1, color) == -1)
                block++;
            count++;
            totalScore += countToScore(count, block, color);
            x = tempX;
            y = tempY;
            count = 0;
            block = 0;
            while (contains(x - 1, y - 1, color) == 1) {
                x--;
                y--;
                count++;
            }
            if (contains(x - 1, y - 1, color) == -1)
                block++;
            x = tempX;
            for (y = tempY; contains(x + 1, y + 1, color) == 1; ) {
                x++;
                y++;
                count++;
            }

            if (contains(x + 1, y + 1, color) == -1)
                block++;
            count++;
            totalScore += countToScore(count, block, color);
            x = tempX;
            y = tempY;
            count = 0;
            block = 0;
            while (contains(x - 1, y + 1, color) == 1) {
                x--;
                y++;
                count++;
            }
            if (contains(x - 1, y + 1, color) == -1)
                block++;
            x = tempX;
            for (y = tempY; contains(x + 1, y - 1, color) == 1; ) {
                x++;
                y--;
                count++;
            }

            if (contains(x + 1, y - 1, color) == -1)
                block++;
            count++;
            totalScore += countToScore(count, block, color);
        }

        return totalScore;
    }

    private int countToScore(int count, int block, Color color) {
        if (block == 2)
            return 0;
        if (count == 5 && color == myColor)
            return 0x3b9aca00;
        if (count == 5 && color == antiColor)
            return 0x5f5e100;
        if (count == 4 && color == myColor && block == 0)
            return 0x989680;
        if (count == 4 && color == myColor && block == 1)
            return 0xf4240;
        if (count == 4 && color == antiColor && block == 0)
            return 0x186a0;
        if (count == 3 && color == myColor && block == 0)
            return 10000;
        if (count == 4 && color == antiColor && block == 1)
            return 1000;
        if (count == 3 && color == myColor && block == 1)
            return 100;
        if (count == 2 && color == myColor && block == 0)
            return 10;
        return count != 2 || color != myColor || block != 1 ? 0 : 1;
    }
}
