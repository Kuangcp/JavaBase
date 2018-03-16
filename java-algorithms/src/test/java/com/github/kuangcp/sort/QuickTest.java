package com.github.kuangcp.sort;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 * @date 18-3-16  下午10:31
 */
public class QuickTest {
    Quick quick = new Quick();

    @Test
    public void testSort() throws Exception {
        Quick.sort(new int[]{0}, 0, 0);
    }

//    @Test
//    public void testQuickSort() throws Exception {
//        T[] result = quick.quickSort(new T[]{new T()}, 0, 0);
//        Assert.assertArrayEquals(new T[]{new T()}, result);
//    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme