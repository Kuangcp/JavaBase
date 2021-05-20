package com.github.kuangcp.validation;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class BeanValidatorTest {

  @Test
  public void testValid() {
    TestParam param = new TestParam();
    param.setState(ActiveState.ACTIVE);
    param.setStr(StrState.SECOND);
    BeanValidator.check(param);
  }

  @Test
  public void testInvalid() {
    TestParam param = new TestParam();
    param.setState(-1);
    param.setStr(StrState.SECOND);
    try {
      BeanValidator.check(param);
      Assert.fail();
    } catch (Exception e) {
      Assert.assertEquals(e.getMessage(), ErrorMsgConstant.STATE_ERROR);
    }
  }

  @Test
  public void testValidStr() {
    TestParam param = new TestParam();
    param.setStr(StrState.SECOND);
    param.setState(ActiveState.NONE);
    BeanValidator.check(param);
  }

  @Test
  public void testInvalidStr() {
    TestParam param = new TestParam();
    param.setStr("2s");
    param.setState(3);
    try {
      BeanValidator.check(param);
      Assert.fail();
    } catch (Exception e) {
      Assert.assertEquals(e.getMessage(), ErrorMsgConstant.STR_STATE_ERROR);
    }
  }
}
