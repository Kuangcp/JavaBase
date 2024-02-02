package netty.timeServer;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author https://github.com/kuangcp on 2020-08-22 14:28
 */
@Slf4j
public class TimeClientTest {

    private final TimeClient timeClient = new TimeClient();

    //    @Test(threadPoolSize = 5, invocationCount = 20)
    @Test
    public void testClient() throws Exception {
        startClient();
        timeClient.sendMsg(Command.QUERY_TIME);
    }

    @Test
    public void testConcurrencyClient() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        startClient();

        int num = 100;
        CountDownLatch countDownLatch = new CountDownLatch(num);
        for (int i = 0; i < num; i++) {
            pool.submit(() -> {
                try {
                    timeClient.sendMsg(Command.QUERY_TIME);
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        // why not flush cache
        timeClient.flush();
        Thread.sleep(4000);

//    timeClient.sendMsg(Command.STOP_SERVER);
        timeClient.stop();
    }

    void startClient() throws InterruptedException {
        if (timeClient.isReady()) {
            return;
        }

        Thread thread = new Thread(() -> {
            try {
                timeClient.connectLocal(TimeServer.port);
            } catch (Exception e) {
                log.error("", e);
            }
        });

        thread.start();

        // 当循环体为空时会陷入死循环，即使已经准备好也没有退出
        while (!timeClient.isReady()) {
//      Thread.sleep(10);
        }
        log.info("client ready");
    }

    @Test
    public void testXX() throws Exception {
        System.out.println(0x80);
    }

    @Test
    public void testNPE() throws Exception {

        for (int i = 0; i < 6000; i++) {
            try {
                String s = null;
                s.charAt(3);
            } catch (Exception e) {
                log.error("", e);
            }
        }
        Thread.sleep(2000);
    }
}

