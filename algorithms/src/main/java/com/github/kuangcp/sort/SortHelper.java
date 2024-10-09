package com.github.kuangcp.sort;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by https://github.com/kuangcp
 * 用来初始化大量数据用于排序
 *
 * @author kuangcp
 */
@Slf4j
class SortHelper {

    // 数量级
    static int AMOUNT = 1000;

    // 数据范围
    static int SCOPE = 999999;
    static boolean show = false;

    static Map<String, Sorter> algorithms = new LinkedHashMap<>();

    static {
        algorithms.put("Bubble", Bubble::sort);
        algorithms.put("Select", Select::sort);
        algorithms.put("Insert", Insert::sort);
        algorithms.put("Radix", Radix::sort);
        algorithms.put("Quick", Quick::sort);
    }

    static int[] data;

    static void init() {
        Radix.maxLen = getScopeLength();

        data = new int[AMOUNT];
        for (int i = 0; i < AMOUNT; i++) {
            data[i] = ThreadLocalRandom.current().nextInt(SCOPE);
        }
    }

    private static int getScopeLength() {
        return String.valueOf(SCOPE).length();
    }

    static void showData(int[] data) {
        if (!show) {
            return;
        }

        for (int i = 0; i < data.length; i++) {
            int length = getScopeLength();
            System.out.printf("%" + length + "d ", data[i]);
            if ((i + 1) % 19 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    static void validate(int[] data) {
        for (int i = 1; i < data.length; i++) {
            if (data[i - 1] > data[i]) {
                log.error("sort algorithm has error");
                Assert.fail();
            }
        }
    }
}