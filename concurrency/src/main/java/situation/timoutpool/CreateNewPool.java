package situation.timoutpool;

import lombok.extern.slf4j.Slf4j;
import situation.timoutpool.base.Param;
import situation.timoutpool.base.Result;
import situation.timoutpool.base.TaskExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 方案： 使用临时线程池
 * 缺陷 遇到突发请求流量时线程大增，有拖垮服务器的风险
 *
 * @author https://github.com/kuangcp on 2021-09-04 23:23
 */
@Slf4j
public class CreateNewPool implements TaskExecutor<Param, Result> {

    @Override
    public Result execute(Param param, long timeout, TimeUnit timeUnit) {
        final LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(1000);
        final Result result = Result.builder().dataList(Collections.synchronizedList(new ArrayList<>()))
                .build();
        final ThreadPoolExecutor pool = new ThreadPoolExecutor(3, 3, 1, TimeUnit.SECONDS,
                workQueue, new ThreadPoolExecutor.DiscardPolicy());
        for (int i = 0; i < param.getTotal(); i++) {
            final Param tmpParam = Param.builder().start(i).build();
            pool.execute(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(600) + 60);
                } catch (InterruptedException e) {
                    log.error("", e);
                }
//                log.info("tmpParam={}", tmpParam);
                result.getDataList().add(tmpParam.toString());
            });
        }

        try {
            pool.shutdown();
            final boolean complete = pool.awaitTermination(timeout, timeUnit);
            if (complete) {
                log.info("complete");
            } else {
                log.info("not complete. workQueue={}", workQueue.size());
            }
        } catch (Exception e) {
            log.error("", e);
        }

        return result;
    }

}
