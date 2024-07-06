package web.situation;

import com.hellokaton.blade.annotation.Path;
import com.hellokaton.blade.annotation.route.GET;
import com.hellokaton.blade.mvc.http.Response;
import lombok.extern.slf4j.Slf4j;
import situation.timoutpool.CreateNewPool;
import situation.timoutpool.base.Param;
import situation.timoutpool.base.Result;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-05 00:56
 */
@Path
@Slf4j
public class TimeoutPoolController {

    @GET("/situation/timeout/createNew")
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
