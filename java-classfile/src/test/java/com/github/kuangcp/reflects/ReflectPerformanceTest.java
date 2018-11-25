package com.github.kuangcp.reflects;

import com.github.kuangcp.time.GetRunTime;
import java.lang.reflect.Method;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.junit.Test;

/**
 * 反射的性能问题 http://www.cnblogs.com/zhishan/p/3195771.html
 *
 * @author kuangcp
 */
@Slf4j
public class ReflectPerformanceTest {

  private static final int LOOP_SIZE = 500_000;

  @Data
  class TestClass {

    private int num;
  }

  @Test
  public void testGetSet() {
    long sum = 0;
    TestClass testClass = new TestClass();

    GetRunTime.GET_RUN_TIME.startCount();
    for (int i = 0; i < LOOP_SIZE; ++i) {
      testClass.setNum(i);
      sum += testClass.getNum();
    }
    GetRunTime.GET_RUN_TIME.endCount("invoke get-set method ");
    log.info("LOOP_SIZE {} 和是 {} ", LOOP_SIZE, sum);
  }

  @Test
  public void testOriginReflect() throws Exception {
    long sum = 0;
    TestClass testClass = new TestClass();

    GetRunTime.GET_RUN_TIME.startCount();
    for (int i = 0; i < LOOP_SIZE; ++i) {
      Method method = testClass.getClass().getMethod("setNum", int.class);
      method.invoke(testClass, i);
      sum += testClass.getNum();
    }

    GetRunTime.GET_RUN_TIME.endCount("simple reflect ");
    log.info("LOOP_SIZE {} 和是 {} ", LOOP_SIZE, sum);
  }

  @Test
  public void testOriginReflectWithCache() throws Exception {
    long sum = 0;
    TestClass testClass = new TestClass();
    GetRunTime.GET_RUN_TIME.startCount();

    Method method = testClass.getClass().getMethod("setNum", int.class);
    for (int i = 0; i < LOOP_SIZE; ++i) {
      method.invoke(testClass, i);
      int num = testClass.getNum();
      sum += num;
    }

    GetRunTime.GET_RUN_TIME.endCount("simple reflect with cache");
    log.info("LOOP_SIZE {} 和是 {} ", LOOP_SIZE, sum);
  }

  @Test
  public void testCglib() throws Exception {
    long sum = 0;
    TestClass testClass = new TestClass();

    FastClass testClazz = FastClass.create(TestClass.class);
    FastMethod method = testClazz.getMethod("setNum", new Class[]{int.class});

    Object[] param = new Object[1];
    GetRunTime.GET_RUN_TIME.startCount();
    for (int i = 0; i < LOOP_SIZE; ++i) {
      param[0] = i;
      method.invoke(testClass, param);
      sum += testClass.getNum();
    }

    GetRunTime.GET_RUN_TIME.endCount("use cglib ");
    log.info("LOOP_SIZE {} 和是 {} ", LOOP_SIZE, sum);
  }

}
