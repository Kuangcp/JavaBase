package com.base;

import com.myth.time.GetRunTime;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp on 18-3-3  下午4:33
 *
 * @author kuangcp
 */
public class StringFinalPool {


    @Test
    public void test1 (){
        String h = "hello";
        String h2 = "he"+"llo";
        String hi = "he"+new String("llo");
        String hil = new String("hello");

        System.out.println(h == h2);
        System.out.println(h == hi);
        System.out.println(hi == hil);

        System.out.println(9%2.0);
    }

    // http://www.nowamagic.net/program/program_SelfAddEffectiveCompare.php
    @Test
    public void test2(){
        GetRunTime time = new GetRunTime();
        time.startCount();
        int  x = 0;
        for (int i = 0; i < 1000000000; i++) {
            x = x+1;
        }
        time.endCount("1");
        time.startCount();
        x = 0;
        for (int i = 0; i < 1000000000; i++) {
            x += 1;
        }
        time.endCount("2");
        time.startCount();
        x = 0;
        for (int i = 0; i < 1000000000; i++) {
            x++;
        }
        time.endCount("3");


    }


}
