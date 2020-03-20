package com.github.kuangcp.lock.pvp;

import java.util.concurrent.locks.ReentrantLock;
import lombok.Data;

/**
 * @author kuangcp on 3/28/19-9:32 PM
 */
@Data
class Player {

  private ReentrantLock lock = new ReentrantLock();

}
