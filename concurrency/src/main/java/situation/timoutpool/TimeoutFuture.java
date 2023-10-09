package situation.timoutpool;

import lombok.extern.slf4j.Slf4j;
import situation.timoutpool.base.Param;
import situation.timoutpool.base.Result;
import situation.timoutpool.base.TaskExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author https://github.com/kuangcp on 2021-09-05 02:45
 */
@Slf4j
public class TimeoutFuture implements TaskExecutor<Param, Result> {

    @Override
    public Result execute(Param param, long timeout, TimeUnit timeUnit) {
        final Result result = Result.builder().dataList(Collections.synchronizedList(new ArrayList<>())).build();
        final CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf();
        for (int i = 0; i < param.getTotal(); i++) {
            final Param tmpParam = Param.builder().start(i).build();
            voidCompletableFuture.thenRunAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(600) + 200);
                } catch (InterruptedException e) {
                    log.error("", e);
                }
//                log.info("tmpParam={}", tmpParam);
                result.getDataList().add(tmpParam.toString());
            });
        }

        try {
            voidCompletableFuture.wait(timeout);
        } catch (InterruptedException e) {
            log.error("", e);
        }

        return null;
    }
}
