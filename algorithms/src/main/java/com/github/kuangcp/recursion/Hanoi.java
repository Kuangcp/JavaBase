package com.github.kuangcp.recursion;

/**
 * 汉诺塔问题的递归实现
 * 
 * 问题描述：
 * 1. 有三根柱子A、B、C，A柱子上有N个盘子，从上到下依次变大
 * 2. 目标是将所有盘子从A移动到C
 * 3. 每次只能移动一个盘子，且大盘子不能放在小盘子上面
 * 
 * @author kuangcp
 */
class Hanoi {

    /**
     * 移动汉诺塔
     *
     * @param n 盘子数量
     * @param from 起始柱子
     * @param auxiliary 辅助柱子
     * @param to 目标柱子
     */
    static void move(int n, char from, char auxiliary, char to) {
        if (n == 1) {
            // 只有一个盘子时，直接移动
            System.out.printf("Move disk 1 from %c to %c%n", from, to);
            return;
        }
        
        // 将n-1个盘子从起始柱移动到辅助柱
        move(n - 1, from, to, auxiliary);
        
        // 将最大的盘子从起始柱移动到目标柱
        System.out.printf("Move disk %d from %c to %c%n", n, from, to);
        
        // 将n-1个盘子从辅助柱移动到目标柱
        move(n - 1, auxiliary, from, to);
    }

    /**
     * 计算移动n个盘子需要的最少步数
     * 使用递归公式：steps(n) = 2 * steps(n-1) + 1
     */
    static int calculateSteps(int n) {
        if (n == 1) {
            return 1;
        }
        return 2 * calculateSteps(n - 1) + 1;
    }
} 