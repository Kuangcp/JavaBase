package thread;

import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

/**
 * @author https://github.com/kuangcp on 2020-05-09 11:01
 */
@Slf4j
public class NotifyAndWait {

  private final String id;
  private final String lock;
  private String result;

  public NotifyAndWait(String lock) {
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
      // TODO 如果对象被多个线程持有锁
//      this.notifyAll();
    }
  }
}
