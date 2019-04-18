package com.github.kuangcp.queue.use.blocking;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 2019-04-19 12:41 AM
 */
@Slf4j
public class VeterinarianTest {

  @Test
  public void testRun() throws Exception {
    BlockingQueue<Appointment<Pet>> lists = new LinkedBlockingQueue<>();
    Appointment<Pet> app = new Appointment<>(new Cat("name"));
    lists.add(app);
    app.getPatient().setName("who");

    lists.add(app);
    lists.add(new Appointment<Pet>(new Cat("name2")));

    Veterinarian veterinarian = new Veterinarian(lists, 2000);
    veterinarian.text = "1";
    veterinarian.start();
// 链表这种对象，实参形参内存共享，所以可以启动后添加
    lists.add(new Appointment<Pet>(new Dog("add")));
    lists.add(new Appointment<Pet>(new Dog("dog2")));
// 队列为空就阻塞了
    log.info("{}", veterinarian);

    veterinarian = new Veterinarian(lists, 1000);
    veterinarian.text = "2";
    veterinarian.start();

    log.info("{}", veterinarian);
  }


}