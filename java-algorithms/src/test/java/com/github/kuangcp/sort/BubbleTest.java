package com.github.kuangcp.sort;

import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 * @date 18-3-16  下午10:28
 */
public class BubbleTest {

    @Test
    public void testSort() {
        Bubble.sort(InitBigData.getData("bubble"));
    }
}