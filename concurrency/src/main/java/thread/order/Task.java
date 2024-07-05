package thread.order;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by https://github.com/kuangcp on 18-1-18  下午1:53
 * 利用原子递增控制线程准入顺序。
 *
 * @author kuangcp
 */
@Slf4j
public class Task implements Runnable {

    private String target;
    private int order;
    private AtomicInteger count; //利用原子递增控制线程准入顺序。但是只限制了执行入口顺序，没有限制任务块原子性的顺序

    public Task(String target, int order, AtomicInteger count) {
        this.target = target;
        this.order = order;
        this.count = count;
    }

    @Override
    public void run() {
        while (true) {
            int count = this.count.get();
            if (count % 3 != order) {
                continue;
            }

            this.count.incrementAndGet();
            log.info("target={} order={} count={}", target, order, count);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                log.error("", e);
            }
        }
    }
}
