package com.github.kuangcp.lock.pvp;

import java.util.concurrent.locks.ReentrantLock;
import lombok.Data;

/**
 * @author kuangcp on 3/28/19-9:32 PM
 * two lock p1 p2 lock p1 p2 un lock
 * TODO lock sort and unlock sort
 */
@Data
class Player {

  private ReentrantLock lock = new ReentrantLock();

}
