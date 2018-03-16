package com.github.kuangcp.sort;

import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 * @date 18-3-16  下午10:31
 */
public class ShellTest {

    @Test
    public void testSort() {
        Shell.sort(InitBigData.getData("shell"));
    }
}