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
    @Test
    public void testSort() {
        Quick.sort(InitBigData.getData("quick"), 0, InitBigData.AMOUNT-1);
    }
}