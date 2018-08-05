package com.github.kuangcp.reflects;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import com.github.kuangcp.time.GetRunTime;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * 也就是说,可以让参数具有多态性, 在反射的时候 参数的类型是一一对应的不存在多态,
 * 当拿到了方法之后, invoke的时候按需放入参数就没有问题
 * @author kuangcp
 */
@Slf4j
public class InvokeByInheritParamTest {

  @Data
  class CommonParam {

    int score;
  }

  @EqualsAndHashCode(callSuper = true)
  @Data
  class RunParam extends CommonParam {

    String id;
  }

  class Logic {

    boolean isError(RunParam param) {
      return param.getScore() > 100;
    }

    boolean isFailed(CommonParam param) {
      return param.getScore() < 60;
    }
  }

  // 标准的反射方式
  @Test
  public void testInvoke() {

    try {
      // 这里不能像我设想的那样 根据 CommonParam 获取到 isError 方法
      Method method = Logic.class.getDeclaredMethod("isError", RunParam.class);

      RunParam runParam = new RunParam();
      runParam.setScore(10);
      verifyInvoke(method, runParam, false);

      runParam.setScore(108);
      verifyInvoke(method, runParam, true);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

  // 但是可以通过在方法上设计参数的多态, 完成共用
  @Test
  public void testInvokeBySuper() {
    try {
      Method method = Logic.class.getDeclaredMethod("isFailed", CommonParam.class);
      verifySuite(method);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

  // 通过两次反射获取 method 并执行
  @Test
  public void testInvokeTryTwice() {
    try {
      Method method;

      // 反射时, 其参数不存在多态的概念
      try {
        // 所以这里会必然报错
        method = Logic.class.getDeclaredMethod("isFailed", RunParam.class);
      } catch (NoSuchMethodException e) {
        method = Logic.class.getDeclaredMethod("isFailed", CommonParam.class);
      }

      verifySuite(method);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

  @Test
  @Ignore
  public void comparePerformance() {

    int concurrent = 10;
    // 经过测试发现 使用异常来控制逻辑, 耗时是直接反射 的 4-7 倍
    GetRunTime record = GetRunTime.INSTANCE;
    record.startCount();
    for (int i = 0; i < concurrent; i++) {
      testInvokeTryTwice();
    }
    record.endCount("twice ");

    record.startCount();
    for (int i = 0; i < concurrent; i++) {
      testInvokeBySuper();
    }
    record.endCount("correct ");
  }

  private void verifySuite(Method method) {
    RunParam runParam = new RunParam();
    runParam.setScore(10);
    verifyInvoke(method, runParam, true);

    runParam.setScore(108);
    verifyInvoke(method, runParam, false);
  }

  private void verifyInvoke(Method method, CommonParam param, boolean expect) {
    boolean result = false;
    try {
      result = (boolean) method.invoke(new Logic(), param);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    assertThat(result, equalTo(expect));
  }
}