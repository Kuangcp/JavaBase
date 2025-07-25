package com.github.kuangcp.recursion;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * 汉诺塔问题的测试类
 *
 * @author kuangcp
 */
public class HanoiTest {

    @Test
    public void testMove() {
        System.out.println("Moving 3 disks from A to C using B as auxiliary:");
        Hanoi.move(3, 'A', 'B', 'C');
    }

    @Test
    public void testCalculateSteps() {
        // 测试不同盘子数量的最少步数
        assertEquals(1, Hanoi.calculateSteps(1));  // 1个盘子需要1步
        assertEquals(3, Hanoi.calculateSteps(2));  // 2个盘子需要3步
        assertEquals(7, Hanoi.calculateSteps(3));  // 3个盘子需要7步
        assertEquals(15, Hanoi.calculateSteps(4)); // 4个盘子需要15步
    }
} 