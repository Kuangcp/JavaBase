package com.math.SimplexMethodQuarter;

import com.math.number.Quarter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Myth on 2017/3/23 0023
 */
public class MethodTest {

    @Test
    public void testMax(){
        List<Quarter> list = new ArrayList<Quarter>();

        list.add(new Quarter(0,1));
        list.add(new Quarter(15,1));
        list.add(new Quarter(5,2));
        SimplexMethod sm = new SimplexMethod();
        Integer index = sm.MaxList(list,true,true);
        if(index!=-1){
            System.out.println(index+"---》"+list.get(index));
        }else {
            System.out.println("没有合适的数据");
        }
        for(Quarter t :list){
            System.out.println(t.toString());
        }
    }
}
