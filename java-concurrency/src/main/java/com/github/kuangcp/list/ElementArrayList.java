package com.github.kuangcp.list;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 3/4/19-8:01 AM
 */
class ElementArrayList {

  private final ArrayList<Element> elements;
  private final ReentrantLock lock;
  private final String name;
  private Iterator<Element> it;

  ElementArrayList(ArrayList<Element> elements, ReentrantLock lock, String name) {
    this.elements = elements;
    this.lock = lock;
    this.name = name;
  }

  void addElement(Element ele) {
    elements.add(ele);
  }

  void prep() {
    it = elements.iterator();//设置迭代器
  }

  void listElement() {
    lock.lock(); // 进行迭代的时候进行 锁定 ，
    try {
//            Thread.sleep(10);
      if (it != null) {
        System.out.print(name + ": ");
        while (it.hasNext()) {
          Element element = it.next();
          System.out.print(element + ", ");
        }
        System.out.println();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
  }
}
