package com.github.kuangcp.strcuture.stackapp;

import java.util.Objects;
import java.util.Stack;
import lombok.Data;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2019-12-29 16:45
 */
@Data
public class GetMin {

  private Stack<Integer> data = new Stack<>();
  private Stack<Integer> min = new Stack<>();

  void pushA(int value) {
    data.push(value);
    if (min.isEmpty()) {
      min.push(value);
    } else if (min.peek() >= value) {
      min.push(value);
    }
  }

  int popA() {
    if (data.isEmpty()) {
      return -1;
    }

    Integer value = data.pop();
    if (!min.isEmpty() && Objects.equals(value, min.peek())) {
      min.pop();
    }
    return value;
  }

  int getMin() {
    return min.peek();
  }

  void pushB(int value) {
    data.push(value);
    if (min.isEmpty()) {
      min.push(value);
    } else if (min.peek() >= value) {
      min.push(value);
    } else {
      min.push(min.peek());
    }
  }

  int popB() {
    if (!min.isEmpty()) {
      min.pop();
    }

    if (!data.isEmpty()) {
      return data.pop();
    }

    return -1;
  }
}
