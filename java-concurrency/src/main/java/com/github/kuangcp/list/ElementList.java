package com.github.kuangcp.list;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 3/4/19-8:01 AM
 */
class ElementList<T> {

  private final CopyOnWriteArrayList<T> elements;
  private final ReentrantLock lock;
  private final String name;
  private Iterator it;

  ElementList(CopyOnWriteArrayList<T> elements, ReentrantLock lock, String name) {
    this.elements = elements;
    this.lock = lock;
    this.name = name;
  }

  void addElement(T ele) {
    elements.add(ele);
  }

  void prep() {
    it = elements.iterator();//设置迭代器
    // 为什么两个线程会得到同一个迭代器
    // 经过groovy中验证，多个线程调用这个得到迭代器的方法的时候会有偶尔出现 返回相同迭代器对象的情况发生
//        System.out.println("得到迭代器"+it);
  }

  void listElement(String who) {
    lock.lock(); // 进行迭代的时候进行 锁定 ，
    System.out.println(who + lock.isLocked() + "遍历 " + it);
    try {
      // 如果这样写的话，就必然只有一个迭代器，因为it是共享的，这是一个属性，
//            if (it ==null){
//                it = elements.iterator();
//            }
      if (it != null) {
        System.out.print(who + name + ": ");
        while (it.hasNext()) {
//                    System.out.println(it);
          System.out.print(it.next() + ", ");
        }
        System.out.println("换行");
      }
    } finally {
      lock.unlock();
    }
    System.out.println();
    // try catch finally 这个结构下来，catch多行注释掉，会影响finally ？？
  }
}
