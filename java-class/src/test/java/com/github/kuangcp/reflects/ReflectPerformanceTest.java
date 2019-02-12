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
 * cglib 耗时 50%-70% 于缓存, 10% 于 原始方式 (已缓存)
 *
 * @author kuangcp
 */
@Slf4j
public class ReflectPerformanceTest {

  private static final int LOOP_SIZE = 5000_000;

  @Data
  class TargetObject {

    private int num;
  }

  // primitive method invoke
  @Test
  public void testGetSet() {
    long sum = 0;
    TargetObject targetObject = new TargetObject();

    GetRunTime.GET_RUN_TIME.startCount();
    for (int i = 0; i < LOOP_SIZE; ++i) {
      targetObject.setNum(i);
      sum += targetObject.getNum();
    }
    GetRunTime.GET_RUN_TIME.endCountOneLine("invoke get-set method ");
    log.info("LOOP_SIZE {} 和是 {} ", LOOP_SIZE, sum);
  }

  @Test
  public void testOriginReflect() throws Exception {
    long sum = 0;
    TargetObject targetObject = new TargetObject();

    GetRunTime.GET_RUN_TIME.startCount();
    for (int i = 0; i < LOOP_SIZE; ++i) {
      Method method = targetObject.getClass().getMethod("setNum", int.class);
      method.invoke(targetObject, i);
      sum += targetObject.getNum();
    }

    GetRunTime.GET_RUN_TIME.endCountOneLine("simple reflect ");
    log.info("LOOP_SIZE {} 和是 {} ", LOOP_SIZE, sum);
  }

  @Test
  public void testOriginReflectWithCache() throws Exception {
    long sum = 0;
    TargetObject targetObject = new TargetObject();
    GetRunTime.GET_RUN_TIME.startCount();

    Method method = targetObject.getClass().getMethod("setNum", int.class);
    for (int i = 0; i < LOOP_SIZE; ++i) {
      method.invoke(targetObject, i);
      int num = targetObject.getNum();
      sum += num;
    }

    GetRunTime.GET_RUN_TIME.endCountOneLine("simple reflect with cache");
    log.info("LOOP_SIZE {} 和是 {} ", LOOP_SIZE, sum);
  }

  @Test
  public void testCglib() throws Exception {
    long sum = 0;
    TargetObject targetObject = new TargetObject();

    FastClass testClazz = FastClass.create(TargetObject.class);
    FastMethod method = testClazz.getMethod("setNum", new Class[]{int.class});

    Object[] param = new Object[1];
    GetRunTime.GET_RUN_TIME.startCount();
    for (int i = 0; i < LOOP_SIZE; ++i) {
      param[0] = i;
      method.invoke(targetObject, param);
      sum += targetObject.getNum();
    }

    GetRunTime.GET_RUN_TIME.endCountOneLine("use cglib ");
    log.info("LOOP_SIZE {} 和是 {} ", LOOP_SIZE, sum);
  }

}
