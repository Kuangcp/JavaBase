package com.github.kuangcp.simpleMethod.number;

import org.junit.After;
import org.junit.Test;

/**
 * Created by Myth on 2017/3/21
 * 深刻感受到测试的重要性，及时性，便捷性
 *
 */
public class QuarterTest {

    @Test
    public void testConstruct(){
        log("测试构造器：");
        String num="-1";
        Quarter q = new Quarter(num);
        log("字符串构造器："+num+"-->"+q.toString());
        Integer d=2,n=-4;
        q = new Quarter(d,n);
        log("分子分母构造器："+d+"/"+n+"-->"+q.toString()+"-->"+q.simple().toString());
        Quarter p = new Quarter(q);
        log("复制对象构造器："+q.toString()+"-->"+p.toString());
    }
    @Test
    public void testPlus() throws Exception{
        log("测试加法：");
        Quarter a = new Quarter(2,3);
        Quarter b = new Quarter(4,3);
        log("结果是 : "+a.toString()+"+"+b.toString()+"-->"+a.plus(b).toString());

    }
    @Test
    public void testReduce(){
        log("测试减法：");
        Quarter a = new Quarter(10,3);
        Quarter b = new Quarter(4,3);
        log("结果是："+a.toString()+"-"+b.toString()+"-->"+a.reduce(b).toString());
    }
    @Test
    public void testDivide()throws Exception{
        log("测试除法：");
        Quarter a = new Quarter(25,1);
        Quarter b = new Quarter(0,3);
        log("结果是："+a.toString()+" / "+b.toString()+"-->"+a.divide(b).toString());
    }
    @Test
    public void testMulti()throws Exception{
        log("测试乘法：");
        Integer temp = 6;
        Quarter a = new Quarter(25,1);
        Quarter b = new Quarter(0,3);
        log("分数相乘："+a.toString()+"*"+b.toString()+"-->"+a.multi(b).toString());
        log("分数和整数相乘："+a.toString()+"*"+temp+"-->"+a.multi(temp).toString());
    }
    @Test
    public void testSimple(){
        log("测试化简函数：");
        Quarter q = new Quarter(-4,8);
        System.out.println(q.toString()+"-->"+q.simple().toString());
    }
    //测试比较函数
    @Test
    public void compare (){
        log("测试比较函数：");
        Quarter a = new Quarter(3,1);
        Quarter b = new Quarter(15,6);
        log(a.toString()+">"+b.toString()+":"+a.bigger(b));
        a = new Quarter(3,2);
        b = new Quarter(15,6);
        log(a.toString()+">"+b.toString()+":"+a.bigger(b));

    }
    @Test
    public  void testIsZero(){
        log("测试判断0函数：");
        Quarter a = new Quarter(0,4);
        log("结果是："+a.toString()+"-->"+a.isZero());
        a = new Quarter(6,4);
        log("结果是："+a.toString()+"-->"+a.isZero());
    }
    @Test
    public  void testIsInfinity(){
        log("测试判断Infinity函数：");
        Quarter a = new Quarter(5,0);
        log("结果是："+a.toString()+"-->"+a.isInfinity());
        a = new Quarter(5,3);
        log("结果是："+a.toString()+"-->"+a.isInfinity());
    }
    @Test
    public  void testUpZero(){
        log("测试判断大于0函数：");
        Quarter a = new Quarter(5,2);
        log("结果是："+a.toString()+"-->"+a.upZero());
        a = new Quarter(-5,3);
        log("结果是："+a.toString()+"-->"+a.upZero());
    }
    private void log(String log){
        System.out.println(log);
    }
    @After
    public void hr(){
        System.out.println("------------");
    }
}
