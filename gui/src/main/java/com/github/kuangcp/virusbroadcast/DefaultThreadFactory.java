package com.github.kuangcp.virusbroadcast;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author https://github.com/kuangcp on 2021-09-05 03:05
 */
class DefaultThreadFactory implements ThreadFactory {

  private final AtomicInteger threadNumber = new AtomicInteger(1);
  private final ThreadGroup group;
  private final String namePrefix;

  DefaultThreadFactory(String namePrefix) {
    SecurityManager var1 = System.getSecurityManager();
    this.group = var1 != null ? var1.getThreadGroup() : Thread.currentThread().getThreadGroup();
    this.namePrefix = namePrefix;
  }

  public Thread newThread(Runnable runnable) {
    Thread var2 = new Thread(this.group, runnable,
        this.namePrefix + "-" + this.threadNumber.getAndIncrement(), 0L);
    if (var2.isDaemon()) {
      var2.setDaemon(false);
    }

    if (var2.getPriority() != 5) {
      var2.setPriority(5);
    }

    return var2;
  }
}
