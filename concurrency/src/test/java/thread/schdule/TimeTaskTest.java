package thread.schdule;

import java.util.Timer;
import java.util.TimerTask;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * @author kuangcp on 18-8-20-下午3:20
 */
@Slf4j
public class TimeTaskTest {

  // timer thread run, ignore main thread status
  public static void main(String[] args) throws InterruptedException {
    new TimeTaskTest().testTask();
  }

  // timer thread will exit after main thread
  @Test
  public void testTask() throws InterruptedException {
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        log.info("run");
      }
    }, 100, 1000);

    Thread.sleep(4000);
    log.info("main");
  }
}
