package com.github.kuangcp.strcuture.stackapp;

import java.util.Stack;
import org.junit.Test;

/**
 * @author kuangcp on 18-10-22-上午9:12
 * using two stack to simulation queues
 */
public class SimulationQueue {

  private Stack<Integer> stackA = new Stack<>();
  private Stack<Integer> stackB = new Stack<>();

  /**
   * 入队操作
   *
   * @param element 入队的元素
   */

  public void enQueue(int element) {
    stackA.push(element);

  }


  /**
   * 出队操作
   */

  public Integer deQueue() {
    if (stackB.isEmpty()) {
      if (stackA.isEmpty()) {
        return null;
      }
      transfer();
    }
    return stackB.pop();
  }


  /**
   * 栈A元素转移到栈B
   */

  private void transfer() {
    while (!stackA.isEmpty()) {
      stackB.push(stackA.pop());
    }
  }


  @Test
  public void testMain() {
    enQueue(1);
    enQueue(2);
    enQueue(3);

    System.out.println(deQueue());

    System.out.println(deQueue());

    enQueue(4);
    enQueue(5);
    enQueue(6);

    System.out.println(deQueue());

    System.out.println(deQueue());
    System.out.println(deQueue());
    System.out.println(deQueue());
  }

}
