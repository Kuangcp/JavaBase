package com.github.kuangcp.valatile
/**
 * Created by https://github.com/kuangcp
 * 指令重排问题
 * @author kuangcp* @date 18-4-2  上午9:20
 *
 * https://www.hans-dev.com/java/double-checked-locking-is-broken.html
 * http://gee.cs.oswego.edu/dl/cpj/jmm.html
 *
 */
class Person {

  Person() {
  }
  private static Person instance = null

  // 双重校验锁, 这种方式是不能保证单例的
  // TODO 为何
  static Person getInstance() {
    if (instance == null) {
      synchronized (Person.class) {
        if (instance == null) {
          instance = new Person()
        }
      }
    }
    return instance
  }
}