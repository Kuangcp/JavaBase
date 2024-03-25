package thread;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import thread.ThreadStatusTransfer.Notify;
import thread.ThreadStatusTransfer.Wait;

/**
 * @author kuangcp on 2019-04-22 9:40 AM
 */
public class ThreadStatusTransferTest {

    @Test
    public void testMain() throws InterruptedException {
        Thread waitThread = new Thread(new Wait(), "WaitThread");
        waitThread.start();

        TimeUnit.SECONDS.sleep(100);

        Thread notifyThread = new Thread(new Notify(), "NotifyThread");
        notifyThread.start();
    }
}
