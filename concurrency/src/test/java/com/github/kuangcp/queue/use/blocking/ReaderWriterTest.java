package com.github.kuangcp.queue.use.blocking;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

        BufferedWriter writer = Files.newBufferedWriter(Paths.get("result.csv"));

        CountDownLatch latch = this.startWriter(channel, writer);
        this.startReader(channel);
        latch.await();
        writer.flush();
        writer.close();
    }

    @Test
    public void testBatchRead() throws Exception {
        ArrayBlockingQueue<List<String>> queue = new ArrayBlockingQueue<>(10);
        QueueChannel<List<String>> channel = new QueueChannel<>(queue);

        BufferedWriter writer = Files.newBufferedWriter(Paths.get("result.csv"));

        CountDownLatch latch = this.startWriter(channel, writer);
        this.startBatchReader(channel);
        latch.await();
        writer.flush();
        writer.close();
    }

    private void startBatchReader(QueueChannel<List<String>> channel) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            TimeUnit.MILLISECONDS.sleep(10);
            for (int j = 0; j < 1000; j++) {
                channel.put(Arrays.asList(i + "-" + j, "vv"));
            }
        }
        channel.stop();
    }

    private void startReader(QueueChannel<List<String>> channel) throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            TimeUnit.MILLISECONDS.sleep(10);
//            log.info("add");
            channel.put(Arrays.asList("33-" + i, "vv"));
        }
        channel.stop();
    }

    private CountDownLatch startWriter(QueueChannel<List<String>> channel, BufferedWriter writer) throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        CountDownLatch latch = new CountDownLatch(1);
        pool.execute(() -> {
            try {
                log.info("start");
                List<List<String>> batch = new ArrayList<>(100);
                while (channel.isRunning()) {
                    // 这里的超时时间按需使用，设置的越长就会接收第一条数据的延迟更高(例如设置1s那单个Writer任务最低会消耗1s时间),但是CPU短时忙轮询（毛刺）的情况好一些
                    List<String> task = channel.poll(50, TimeUnit.MILLISECONDS);
                    if (Objects.isNull(task)) {
                        continue;
                    }
                    batch.add(task);
                    TimeUnit.MILLISECONDS.sleep(2);
                    if (batch.size() >= 10) {
                        for (List<String> row : batch) {
                            writer.write(String.join(",", row) + "\n");
                        }
                        log.info("task={}", batch.size());
                        batch.clear();
                    }
                }

                // 很关键，如果读方批次读，读完stop了，消费方还没处理完队列时，得将队列中剩余的数据都读出来，否则就少数据了
                channel.drainTo(batch);

                if (!batch.isEmpty()) {
                    log.info("end task={}", batch.size());
                    for (List<String> row : batch) {
                        writer.write(String.join(",", row) + "\n");
                    }
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
        System.out.println("Start poll 50ms");
        // poll 50ms
        log.info("QUEUE={}", avg("677,677,679,680,110,110,127,136,187,147,119,3840,172,1537,1624,185,13492,13035,116,153,2125,6313,17584")); // 包含线程启动成本
        log.info("QUEUE={}", avg("102,102,101,146,107,104,78,132,166,147,164,3948,159,1507,1648,150,11921,11749,113,143,2124,6045,16872"));
        log.info("NONE ={}", avg("55,52,52,92,52,51,65,72,438,94,101,5547,101,4108,4176,92,15316,15068,66,99,7270,20601,3570"));
        log.info("NONE ={}", avg("52,51,51,97,52,54,64,70,421,94,125,5266,95,4035,4191,95,15743,15638,71,119,7084,20568,3496"));

        System.out.println("Start poll 50ms");
        log.info("NONE ={}", avg("283,289,265,306,54,53,67,73,455,97,5864,5416,104,102,130,129,117,110,98,1839,6493,7127,8612,14486,98,115,3485,3399,14345,14287,67,97,92,3642,6931,21040")); // 包含线程启动成本
        log.info("QUEUE={}", avg("106,106,103,149,102,103,63,117,145,143,4009,4323,152,155,149,150,150,150,211,481,5613,5891,7703,13397,161,182,1307,1309,11543,11501,123,199,204,2147,6122,18479"));
        log.info("NONE ={}", avg("54,55,57,120,54,52,82,76,409,89,5228,5004,99,103,107,111,94,95,102,2004,6804,7099,9211,15073,119,129,3180,3417,14186,14148,106,96,96,3638,7399,20458"));
        log.info("QUEUE={}", avg("101,101,101,145,101,101,164,131,149,143,3870,3922,170,171,175,171,145,144,177,424,4703,4681,7090,12207,156,175,1301,1328,12619,12382,145,198,200,2145,6203,18045"));

        System.out.println("Start poll 50ms");
        // writer时不用缓冲队列，读一条就写一条 但是Excel缺数据了，应该是数据没flush到，但是EasyExcel没flush接口暴露 csv才有
        log.info("QUEUE={}", avg("272,272,272,272,67,63,59,52,137,67,4335,4494,65,67,64,65,63,64,64,418,4937,4861,6861,12732,71,65,1300,1372,12305,12654,52,66,86,2357,6872,18460")); // 包含线程启动成本
        log.info("QUEUE={}", avg("52,50,51,66,50,52,52,55,119,63,4613,4914,65,67,66,67,63,65,65,387,5175,5236,7604,12996,65,65,1199,1304,12848,12969,69,66,74,2060,6450,18039"));

        System.out.println("Start poll 20ms");
        log.info("QUEUE={}", avg("280,318,319,360,74,73,100,94,163,140,4205,4357,116,114,133,134,125,115,135,528,5209,5213,7350,13044,146,167,1237,1317,12525,12616,84,138,136,1961,6017,17213")); // 包含线程启动成本
        log.info("QUEUE={}", avg("78,78,79,113,72,72,52,105,216,127,4121,4297,118,121,138,138,119,118,156,491,4674,4943,7396,12626,114,140,1367,1621,12460,13197,84,121,126,2081,5878,17368"));
        log.info("QUEUE={}", avg("72,72,72,115,74,71,86,88,159,110,3874,3899,114,124,116,117,113,115,138,503,4964,5029,7201,12581,118,144,1435,1483,12354,12123,92,117,112,1992,5825,16973"));

    }

    private static IntSummaryStatistics avg(String x) {
        return Stream.of(x.split(",")).mapToInt(Integer::parseInt).summaryStatistics();
    }

}
