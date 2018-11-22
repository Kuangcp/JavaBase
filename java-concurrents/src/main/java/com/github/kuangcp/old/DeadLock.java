package com.github.kuangcp.old;

/**
 *
 */
public class DeadLock {

  private final String id;

  public DeadLock(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public synchronized void preparRun(Food food, DeadLock lock) {
    System.out.println(
        "准备 currentId: " + id + " resource: " + food.getName() + " prepar other: " + lock.getId());
    try {
      Thread.sleep(120);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    lock.confirmRun(food, this);
  }

  public synchronized void confirmRun(Food food, DeadLock lock) {
    System.out.println(
        "确认 currentId: " + id + " resource: " + food.getName() + "confirm other: " + lock.getId());
  }
}