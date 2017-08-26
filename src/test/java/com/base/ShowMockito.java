package com.base;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by https://github.com/kuangcp on 17-8-26  上午10:59
 * 模拟类库 Mockito 的学习使用
 */
public class ShowMockito {

    @Test
    public void testNums(){
        Nums nums = mock(Nums.class);
        when(nums.plus(new Nums("2","1"))).thenReturn(new Nums("21","1"));
        verify(nums).getBig();

    }
}

class Nums{
    private String big;
    private String dot;

    public Nums(String big, String dot) {
        this.big = big;
        this.dot = dot;
    }

    public Nums plus(Nums other){
        this.big += other.big;
        this.dot += other.dot;
        return this;
    }

    public String getBig() {
        return big;
    }

    public void setBig(String big) {
        this.big = big;
    }

    public String getDot() {
        return dot;
    }

    public void setDot(String dot) {
        this.dot = dot;
    }
}