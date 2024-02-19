package com.github.kuangcp.recursion;

/**
 * @author kuangcp on 3/28/19-9:28 PM
 */
class Fibonacci {

    /**
     * https://bbs.pediy.com/thread-123051.htm
     * 递归解法, 重复计算太多 时间复杂度: 2^(N/2) < T(N) < 2^(N)
     */
    static int recursiveOne(int num) {
        if (num < 0) {
            return 0;
        }

        if (num == 1) {
            return 1;
        }

        return recursiveOne(num - 1) + recursiveOne(num - 2);
    }

    /**
     * 迭代方式, 一直缓存两个值 当前值 前一个值, 并依次后移
     * 时间复杂度 O(N)
     */
    static int loopOne(int num) {
        if (num <= 0) {
            return 0;
        }
        if (num == 1) {
            return 1;
        }

        int pre = 1;
        int cur = 1;

        for (int i = 0; i < num - 2; i++) {
            int temp = pre;
            pre = cur;
            cur = temp + cur;
        }

        return cur;
    }

    /**
     * 构造等比数列, 线性代数, 特征方程, 母函数法 等方式可以求解通项公式
     *
     * fi(n) =
     *
     * 性能最好
     */
    static int generalTermFormula(int num) {
        double temp = Math.sqrt(5);
        return (int) (1 / temp * (Math.pow((1 + temp) / 2, num) - Math.pow((1 - temp) / 2, num)));
    }
}
