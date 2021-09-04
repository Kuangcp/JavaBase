package cmd.situation;

import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.http.Response;
import lombok.extern.slf4j.Slf4j;
import situation.timoutpool.CreateNewPool;
import situation.timoutpool.Param;
import situation.timoutpool.Result;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author https://github.com/kuangcp on 2021-09-05 00:56
 */
@Path
@Slf4j
public class TimeoutPoolController {

    @GetRoute("/situation/timeout/createNew")
    public void createNew(Response response) throws InterruptedException {
        int loop = 60;
        final CountDownLatch latch = new CountDownLatch(loop);
        final ExecutorService exe = Executors.newFixedThreadPool(loop / 15);
        for (int i = 0; i < loop; i++) {
            exe.execute(() -> {
                final CreateNewPool pool = new CreateNewPool();
                final long start = System.nanoTime();
                final Result result = pool.execute(Param.builder().start(1).total(40).build(), 5, TimeUnit.SECONDS);
                log.info("result={} {}ms", result, (System.nanoTime() - start) / 1000_000);
                latch.countDown();
            });
        }
        latch.await();
        response.body("complete: " + loop);
    }
}
