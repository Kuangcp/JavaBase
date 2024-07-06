package situation.timoutpool;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import situation.timoutpool.base.Param;
import situation.timoutpool.base.Result;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-04 23:42
 */
@Slf4j
public class TimeoutPoolTest {

    @Test
    public void testCreateNew() throws Exception {
        int loop = 10;
        final CountDownLatch latch = new CountDownLatch(loop);
        final ExecutorService exe = Executors.newFixedThreadPool(loop);
        for (int i = 0; i < loop; i++) {
            exe.execute(() -> {
                final CreateNewPool pool = new CreateNewPool();
                final long start = System.nanoTime();
                final Result result = pool.execute(Param.builder().start(1).total(40).build(), 5,
                        TimeUnit.SECONDS);
                log.info("{}ms {}", (System.nanoTime() - start) / 1000_000, result);
                latch.countDown();
//                assertThat(result.getDataList(), equalTo(40));
                log.info("size={}", result.getDataList().size());
            });
        }
        latch.await();
        log.info("end");
    }

    @Test
    public void testOnce() {
        final CreateNewPool pool = new CreateNewPool();
        final long start = System.nanoTime();
        final Result result = pool.execute(Param.builder().start(1).total(40).build(), 5,
                TimeUnit.SECONDS);
        log.info("{}ms {}", (System.nanoTime() - start) / 1000_000, result);
//                assertThat(result.getDataList(), equalTo(40));
        log.info("size={}", result.getDataList().size());
    }
}