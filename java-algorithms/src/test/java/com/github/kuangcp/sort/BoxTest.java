package com.github.kuangcp.sort;

import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 * @date 18-3-16  下午10:28
 */
public class BoxTest {
    Box box = new Box();

    @Test
    public void testSort() throws Exception {
        box.sort(new int[]{0});
    }
}