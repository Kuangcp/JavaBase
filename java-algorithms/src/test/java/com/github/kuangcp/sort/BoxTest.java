package com.github.kuangcp.sort;

import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 * @date 18-3-16  下午10:28
 */
public class BoxTest {

    @Test
    public void testSort() {
        Box box = new Box();
        box.sort(InitBigData.getData("box"));
    }
}