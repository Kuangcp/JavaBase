package com.github.kuangcp.queue.use.blocking;

import java.util.concurrent.BlockingQueue;
import lombok.extern.slf4j.Slf4j;

// 因为父类没有无参构造器，所以子类也没有，子类也要显式声明 宠物类
// 接待类,所有的宠物实例先达到这里再给兽医
@Slf4j
public class Veterinarian extends Thread {

  protected final BlockingQueue<Appointment<Pet>> pets;
  protected String text = "";
  protected final int restTime; // 预约之间的休息时间
  private boolean shutdown = false;

  Veterinarian(BlockingQueue<Appointment<Pet>> pets, int pause) {
    this.pets = pets;
    restTime = pause;
  }

  synchronized void shutdown() {
    shutdown = true;
  }

  @Override
  public void run() {
    while (!shutdown) {
      seePatient();
      try {
        sleep(restTime);
      } catch (InterruptedException ignored) {
        shutdown = true;
      }
    }
  }

  /**
   * 线程从队列中取出预约， 进行操作，如果当前队列没有预约， 就会阻塞
   */
  void seePatient() {
    try {
      Appointment<Pet> ap = pets.take(); // 阻塞take
      Pet patient = ap.getPatient();
      log.info(text + " take " + patient.name);
      patient.examine();
    } catch (InterruptedException ignored) {
      shutdown = true;
    }
  }

}

