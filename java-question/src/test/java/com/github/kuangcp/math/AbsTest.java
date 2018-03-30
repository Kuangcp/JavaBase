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

    public void show(double a){
        System.out.println("plus: "+a);
    }

    public double caculate(double numA, double numB){
        if(numA > 6){
            show(numA);
        }
        return numA + numB;
    }
    @Test
    public void testAbs(){
        System.out.println(Math.abs(222 - 222)<100);
    }

    // 实例化一个Map后, 因为没有元素在里面, 所以返回Null

    @Test
    public void testHashMap(){
        Map<Integer, String> map = new HashMap<>();
        String result = map.get(1);
        System.out.println(result);

    }

    @Test
    public void testDebug(){

        for (int i = 3; i < 10; i++) {
            // 在下面一行打上断点, 然后就能用 F8 单步调试
            assert caculate(i, i+3) > 7;

        }
    }
}
