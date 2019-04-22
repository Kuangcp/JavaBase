package jvm.oom;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;

/**
 * -Xms5m -Xmx5m
 *
 * @author kuangcp on 4/5/19-9:45 AM
 */
@Ignore
@Slf4j
public class HeapOOMTest {

  private HeapOOM heapOOM = new HeapOOM();

  @Test
  public void testOOMWithOtherThread() throws Exception {
    // 如果将该集合放在这里, 那么就不会被回收, 导致OOM 所有线程都玩完
    // List<byte[]> data = new ArrayList<>();

    Thread main = new Thread(() -> {
      List<byte[]> data = new ArrayList<>();
      while (true) {
        // 因为当这里无法分配内存 OOM 后就出循环了, 对象也就可以被回收了
        data.add(new byte[1024 * 256]);
        try {
          TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        log.info("allocate memory");
      }
    });

    Thread thread = new Thread(() -> {
      while (true) {
        try {
          TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        log.info("timer");
      }
    });

    main.start();
    thread.start();

    main.join();
    thread.join();
  }

  @Test
  public void testCreateArray() {
    heapOOM.createArray();
  }

  @Test
  @Ignore
  public void testCreateMap() throws Exception {
    heapOOM.createMap();
  }
}