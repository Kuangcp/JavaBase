package thread.waitnotify;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

/**
 * wait and notify
 *
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2020-05-09 11:01
 */
@Slf4j
public class CallBackHolder {

    private final String id;
    private final String lock;
    private String result;

    public CallBackHolder(String lock) {
        this.id = UUID.randomUUID().toString();
        this.lock = lock;
    }

    public Optional<String> call() throws InterruptedException {
        synchronized (lock) {
            Thread.sleep(1000);
        }
        synchronized (this) {
            log.info("start wait {}", id);
            this.wait(2000L);
        }
        return Optional.ofNullable(result).map(v -> v + "  " + id);
    }

    public void back(String result) {
        this.result = result;
        log.info("set={}", result);

        synchronized (this) {
            this.notify();
            // 如果对象被多个线程等待锁，就需要唤醒全部的等待线程
//            this.notifyAll();
        }
    }
}
