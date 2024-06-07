package com.github.kuangcp.queue.use.blocking;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @author Kuangcp
 * 2024-06-07 15:02
 */
@Slf4j
public class ReaderWriterTest {


    @Test
    public void testChannel() throws Exception {
        ArrayBlockingQueue<List<String>> queue = new ArrayBlockingQueue<>(10);
        QueueChannel<List<String>> channel = new QueueChannel<>(queue);

        CountDownLatch latch = startWriter(channel);
        startReader(channel);
        latch.await();
    }

    private void startReader(QueueChannel<List<String>> channel) throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            TimeUnit.MILLISECONDS.sleep(100);
            log.info("add");
            channel.put(Arrays.asList("33-" + i, "vv"));
        }
        channel.stop();
    }

    private CountDownLatch startWriter(QueueChannel<List<String>> channel) {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        CountDownLatch latch = new CountDownLatch(1);
        pool.execute(() -> {
            try {
                log.info("start");
                List<List<String>> batch = new ArrayList<>(100);
                while (channel.isRunning()) {
                    List<String> task = channel.poll(1, TimeUnit.SECONDS);
                    if (Objects.isNull(task)) {
                        continue;
                    }
                    batch.add(task);
                    TimeUnit.MILLISECONDS.sleep(500);
                    if (batch.size() >= 10) {
                        log.info("task={}", batch.size());
                        batch.clear();
                    }
                }
                if (!batch.isEmpty()) {
                    log.info("end task={}", batch.size());
                    batch.clear();
                }
            } catch (Exception e) {
                log.error("", e);
            } finally {
                latch.countDown();
            }
        });
        return latch;
    }


    @Test
    public void testTime() throws Exception {
        log.info("average={}", avg("677,677,679,680,110,110,127,136,187,147,119,3840,172,1537,1624,185,13492,13035,116,153,2125,6313,17584"));
        log.info("average={}", avg("102,102,101,146,107,104,78,132,166,147,164,3948,159,1507,1648,150,11921,11749,113,143,2124,6045,16872"));
        log.info("average={}", avg("55,52,52,92,52,51,65,72,438,94,101,5547,101,4108,4176,92,15316,15068,66,99,7270,20601,3570"));
        log.info("average={}", avg("52,51,51,97,52,54,64,70,421,94,125,5266,95,4035,4191,95,15743,15638,71,119,7084,20568,3496"));


        // 包含线程启动成本
        log.info("NONE ={}", avg("283,289,265,306,54,53,67,73,455,97,5864,5416,104,102,130,129,117,110,98,1839,6493,7127,8612,14486,98,115,3485,3399,14345,14287,67,97,92,3642,6931,21040"));
        log.info("QUEUE={}", avg("106,106,103,149,102,103,63,117,145,143,4009,4323,152,155,149,150,150,150,211,481,5613,5891,7703,13397,161,182,1307,1309,11543,11501,123,199,204,2147,6122,18479"));

        log.info("NONE ={}", avg("54,55,57,120,54,52,82,76,409,89,5228,5004,99,103,107,111,94,95,102,2004,6804,7099,9211,15073,119,129,3180,3417,14186,14148,106,96,96,3638,7399,20458"));
        log.info("QUEUE={}", avg("101,101,101,145,101,101,164,131,149,143,3870,3922,170,171,175,171,145,144,177,424,4703,4681,7090,12207,156,175,1301,1328,12619,12382,145,198,200,2145,6203,18045"));

        // writer方不用缓冲队列，读一条就写一条 但是出bug了，缺数据，应该是数据没flush到，但是没flush接口暴露
        // 包含线程启动成本
        log.info("QUEUE={}", avg("272,272,272,272,67,63,59,52,137,67,4335,4494,65,67,64,65,63,64,64,418,4937,4861,6861,12732,71,65,1300,1372,12305,12654,52,66,86,2357,6872,18460"));
        log.info("QUEUE={}", avg("52,50,51,66,50,52,52,55,119,63,4613,4914,65,67,66,67,63,65,65,387,5175,5236,7604,12996,65,65,1199,1304,12848,12969,69,66,74,2060,6450,18039"));
    }

    private static IntSummaryStatistics avg(String x) {
        return Stream.of(x.split(",")).mapToInt(Integer::parseInt).summaryStatistics();
    }

}
