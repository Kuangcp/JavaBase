package com.github.kuangcp.strcuture.stackapp;

import com.github.kuangcp.strcuture.stacks.LinkStack;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

/**
 * https://github.com/kuangcp
 *
 * @author kuangcp on 18-8-27-上午12:27
 */
@Slf4j
public class AddTwoBigInteger {

  private LinkStack oneStack = new LinkStack();
  private LinkStack otherStack = new LinkStack();
  private LinkStack resultStack = new LinkStack();

  private boolean initBigInteger(String target, LinkStack stack) {
    String regex = "^[0-9]+$";
    boolean oneResult = Pattern.matches(regex, target);
    if (!oneResult) {
      log.error("there are not integer: one={}", target);
      return false;
    }
    boolean isMoreThanZero = false;
    for (int i = 0; i < target.length(); i++) {
      char c = target.charAt(i);
      if (c != '0') {
        isMoreThanZero = true;
      }
      if (isMoreThanZero) {
        stack.push(target.charAt(i));
      }
    }
    return true;
  }

  public String add(String one, String other) {
    boolean oneResult = initBigInteger(one, oneStack);
    boolean otherResult = initBigInteger(other, otherStack);
    if (!oneResult || !otherResult) {
      return "";
    }

    int flag = 0;
    while (!oneStack.isEmpty() && !otherStack.isEmpty()) {
      int sum;
      sum = oneStack.pop() + otherStack.pop() + flag - 96;
      flag = 0;
      if (sum >= 10) {
        sum -= 10;
        flag = 1;
      }
      resultStack.push(sum);
    }
    if (!oneStack.isEmpty() && otherStack.isEmpty()) {
      flag = pushResult(flag, oneStack);
    } else if (oneStack.isEmpty() && !otherStack.isEmpty()) {
      flag = pushResult(flag, otherStack);
    }
    if (flag == 1) {
      resultStack.push(flag);
    }
    return resultStack.display();
  }

  private int pushResult(int flag, LinkStack oneStack) {
    while (!oneStack.isEmpty()) {
      int t = oneStack.pop() + flag - 48;
      flag = 0;
      if (t >= 10) {
        t -= 10;
        flag = 1;
      }
      resultStack.push(t);
    }
    return flag;
  }

  public void clear() {
    oneStack.clear();
    otherStack.clear();
    resultStack.clear();
  }
}
