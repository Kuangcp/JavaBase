// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CheckWin.java

package com.github.kuangcp.gomoku;

import java.awt.*;

public class CheckWin {

    public CheckWin() {
    }

    public static boolean check(Color chess[][], int x, int y, Color color) {
        int count = 0;
        int tempX = x;
        int tempY = y;
        for (; contains(chess, x - 1, y, color); x--)
            count++;

        x = tempX;
        for (y = tempY; contains(chess, x + 1, y, color); x++)
            count++;

        if (count + 1 >= 5)
            return true;
        count = 0;
        x = tempX;
        for (y = tempY; contains(chess, x, y - 1, color); y--)
            count++;

        x = tempX;
        for (y = tempY; contains(chess, x, y + 1, color); y++)
            count++;

        if (count + 1 >= 5)
            return true;
        count = 0;
        x = tempX;
        for (y = tempY; contains(chess, x - 1, y - 1, color); y--) {
            count++;
            x--;
        }

        x = tempX;
        for (y = tempY; contains(chess, x + 1, y + 1, color); y++) {
            count++;
            x++;
        }

        if (count + 1 == 5)
            return true;
        count = 0;
        x = tempX;
        for (y = tempY; contains(chess, x - 1, y + 1, color); y++) {
            count++;
            x--;
        }

        x = tempX;
        for (y = tempY; contains(chess, x + 1, y - 1, color); y--) {
            count++;
            x++;
        }

        return count + 1 == 5;
    }

    private static boolean contains(Color chess[][], int x, int y, Color color) {
        if (x < 0 || x > 14 || y < 0 || y > 14)
            return false;
        return chess[x][y] == color;
    }
}
