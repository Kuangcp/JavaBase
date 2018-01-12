package com.generic.simple;

import org.apache.poi.ss.formula.functions.T;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.mockito.Mockito.*;

/**
 * Created by https://github.com/kuangcp on 18-1-11  下午5:38
 * 这个泛型类的使用就有点像集合的泛型使用了
 * 理解为模板类?
 * @author kuangcp
 */
public class PairTest {
    @Test
    public void testsimple(){
        Pair<Date> pair = new Pair<>();
        pair.setFirst(new Date());
        System.out.println(pair.getFirst());

        // 因为实例化这个类的时候就已经声明了类型为Date,所以里面的所有的T都会替换成Date
        // 下面再用别的参数的话就会编译报错,所以用来约束类型很方便
//        pair.setSecond(new Integer(3));

    }

    @Test
    public void testMinMax(){

    }

}