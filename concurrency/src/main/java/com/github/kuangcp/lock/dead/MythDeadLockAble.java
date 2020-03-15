package com.github.kuangcp.lock.dead;

import com.github.kuangcp.old.Food;

/**
 * @author https://github.com/kuangcp on 2020-03-16 00:47
 */
public interface MythDeadLockAble {

  void prepareRun(Food food, MythDeadLockAble lock);

  String getId();

  void confirmRun(Food food, MythDeadLockAble lock);

  boolean confirmRun2(Food food, MythDeadLockAble lock);
}
