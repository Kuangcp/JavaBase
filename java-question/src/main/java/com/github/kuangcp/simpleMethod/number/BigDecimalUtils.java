package com.github.kuangcp.simpleMethod.number;

import java.math.BigDecimal;

/**
 * Created by Myth on 2017/3/21
 * BigDecimal 是一个精确的浮点数，采用的是String来计算的，相对于Double和Float要精确些，但还是会有精度的丢失
 */
public class BigDecimalUtils {

    public static void main(String []s){
        Double number = 77.777777777777777777;
        System.out.println("double     "+number);
        BigDecimal bigDecimal = new BigDecimal(77.7777777777777777777777);
        System.out.println("BigDecimal "+ bigDecimal.toString());
        // 存储的精度都一样, 后者反而丢失了精度, 这个.........
    }
}
