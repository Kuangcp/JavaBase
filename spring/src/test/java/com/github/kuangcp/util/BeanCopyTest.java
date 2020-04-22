package com.github.kuangcp.util;

import java.util.concurrent.TimeUnit;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

/**
 * @author https://github.com/kuangcp on 2020-04-21 20:31
 */
public class BeanCopyTest {


  // 如何复现大量反射类 GeneratedMethodAccessor 装载和卸载
  @Test
  public void testCopy() throws InterruptedException {
    @Data
    @NoArgsConstructor
    class A {

      String name;
      String info;

      @Override
      protected void finalize() throws Throwable {
        super.finalize();
        System.out.print("a final  ");
      }
    }
    @NoArgsConstructor

    @Data
    class B {

      String name;
      String info;
      String len;

      @Override
      protected void finalize() throws Throwable {
        super.finalize();
        System.out.print("b final ");
      }
    }

    for (int i = 0; i < 100; i++) {
      for (int j = 0; j < 10000; j++) {
        A a = new A();
        a.name = "name";
        a.info = "info";

        B b = new B();

        BeanUtils.copyProperties(a, b);
        BeanUtils.copyProperties(b, a);
      }

      TimeUnit.SECONDS.sleep(5);
      System.out.println("loop");
    }
  }
}
