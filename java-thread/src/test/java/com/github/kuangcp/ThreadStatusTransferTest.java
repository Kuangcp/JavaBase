package com.github.kuangcp;

import com.github.kuangcp.ThreadStatusTransfer.Notify;
import com.github.kuangcp.ThreadStatusTransfer.Wait;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * @author kuangcp on 2019-04-22 9:40 AM
 */
public class ThreadStatusTransferTest {

  @Test
  public void testMain() throws InterruptedException {
    Thread waitThread = new Thread(new Wait(), "WaitThread");
    waitThread.start();

    TimeUnit.SECONDS.sleep(1);

    Thread notifyThread = new Thread(new Notify(), "NotifyThread");
    notifyThread.start();
  }
}
