package com.github.kuangcp.strcuture.stackapp;

import com.github.kuangcp.strcuture.stack.MythBaseStack;
import com.github.kuangcp.strcuture.stack.MythLinkedStack;
import java.util.LinkedList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * https://github.com/kuangcp
 *
 * @author kuangcp on 18-8-27-下午11:46
 */
@Slf4j
public class MatchBracket {

  private List<Character> leftBrackets = new LinkedList<>();
  private List<Character> rightBrackets = new LinkedList<>();

  private void initBrackets() {
    leftBrackets.add('[');
    leftBrackets.add('{');
    leftBrackets.add('(');

    rightBrackets.add(']');
    rightBrackets.add('}');
    rightBrackets.add(')');
  }

  public void match(String origin) {
    log.info("input string={}", origin);
    initBrackets();

    MythBaseStack<Integer> stack = new MythLinkedStack<>();

    for (int i = 0; i < origin.length(); i++) {
      char charAt = origin.charAt(i);

      if (leftBrackets.contains(charAt)) {
        stack.push((int) charAt);
        continue;
      }
      if (rightBrackets.contains(charAt)) {
        int pop = stack.pop();
        if (charAt == ')') {
          if (pop != '(') {
            stack.push((int) charAt);
          }
        }
        if (charAt == ']') {
          if (pop != '[') {
            stack.push((int) charAt);
          }
        }
        if (charAt == '}') {
          if (pop != '{') {
            stack.push((int) charAt);
          }
        }
      }
    }
    while (!stack.isEmpty()) {
      log.error("char={}", stack.pop());
    }
  }
}
