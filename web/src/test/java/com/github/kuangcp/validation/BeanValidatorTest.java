package com.github.kuangcp.validation;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class BeanValidatorTest {

  @Test
  public void testValid() {
    TestParam d = new TestParam();
    d.setState(ActiveState.ACTIVE);
    BeanValidator.check(d);
  }

  @Test
  public void testInvalid() {
    TestParam d = new TestParam();
    d.setState(-1);
    try {
      BeanValidator.check(d);
    } catch (Exception e) {
      Assert.assertEquals(e.getMessage(), "状态不合法");
      return;
    }
    Assert.fail();
  }

  @Test
  public void testValidStr() {
    TestParam d = new TestParam();
    d.setStr("2");
    d.setState(ActiveState.NONE);
    BeanValidator.check(d);
  }

  @Test
  public void testInvalidStr() {
    TestParam d = new TestParam();
    d.setStr("2s");
    d.setState(3);
    try {
      BeanValidator.check(d);
    } catch (Exception e) {
      Assert.assertEquals(e.getMessage(), "状态str不合法");
      return;
    }
    Assert.fail();
  }
}
