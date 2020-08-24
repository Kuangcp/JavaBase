package com.github.kuangcp.virusbroadcast;

import com.github.kuangcp.virusbroadcast.domain.City;
import com.github.kuangcp.virusbroadcast.gui.DisplayPanel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JFrame;

public class VirusBroadcast {

  private static final ExecutorService executors = new ThreadPoolExecutor(2, 2, 0L,
      TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new DefaultThreadFactory("virus"));

  public static void main(String[] args) {
    DisplayPanel panel = new DisplayPanel();
    JFrame frame = new JFrame();
    frame.add(panel);
    frame.setSize(1000, 800);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    executors.submit(panel);
    executors.submit(City.getInstance());
  }
}

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