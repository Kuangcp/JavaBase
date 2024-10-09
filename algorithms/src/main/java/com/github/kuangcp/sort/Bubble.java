package com.github.kuangcp.sort;

import java.util.Arrays;

/**
 * 冒泡排序，从小到大
 * 最坏的情况：O(n^2)
 *
 * @author kcp
 */
public class Bubble {

    public static int[] sort(int[] data) {
        int[] result = Arrays.copyOf(data, data.length);

        for (int i = 1; i < result.length; i++) {
            //用来冒泡的语句，0到已排序的部分
            for (int j = 0; j < result.length - i; j++) {
                //大就交换，把最大的沉入最后
                if (result[j] > result[j + 1]) {
                    int temp = result[j + 1];
                    result[j + 1] = result[j];
                    result[j] = temp;
                }
            }
        }
        return result;
    }
}
