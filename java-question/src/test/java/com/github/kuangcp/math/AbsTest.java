package com.github.kuangcp.math;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 * @date 18-3-21  下午1:07
 */
public class AbsTest {

    @Test
    public void testAbs(){
        System.out.println(Math.abs(222 - 222)<100);
    }

    @Test
    public void testHashMap(){
        Map<Integer, String> map = new HashMap<>();
        String result = map.get(1);
        System.out.println(result);

    }
}
