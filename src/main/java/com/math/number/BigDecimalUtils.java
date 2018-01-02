package com.math.number;

import java.math.BigDecimal;

/**
 * Created by Myth on 2017/3/21
 * BigDecimal 是一个精确的浮点数，采用的是String来计算的，相对于Double和Float要精确些，但还是会有精度的丢失
 * TODO todo
 */
public class BigDecimalUtils {

    public static void main(String []s){
        Double number = 77.77777777777777777777777777;
        System.out.println(number);
        BigDecimal bigDecimal = new BigDecimal(77.77777777777777777777777777);
        System.out.println(bigDecimal.toString());
    }
}
