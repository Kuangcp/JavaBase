package com.github.kuangcp.lock.pvp;

import com.github.kuangcp.tuple.Tuple2;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author kuangcp on 2019-04-21 11:14 AM
 *
 * 加锁顺序问题测试， 结论 加锁需要顺序，释放锁无需顺序
 */
@Slf4j
public class PlayerTest {

  private Map<String, Player> playerMap = new HashMap<>();

  private void arenaLogic(Player a, Player b) throws InterruptedException {
    log.info("enter arena: a={} b={}", a, b);
    Thread.sleep(ThreadLocalRandom.current().nextInt(40));
  }

  private void init() {
    Player a = new Player();
    Player b = new Player();
    Player c = new Player();

    playerMap.put("a", a);
    playerMap.put("b", b);
    playerMap.put("c", c);
  }

  private Tuple2<String, String> randomPlayer() {
    ArrayList<String> list = new ArrayList<>(playerMap.keySet());

    int aIndex = 0;
    int bIndex = 0;
    while (aIndex == bIndex) {
      aIndex = ThreadLocalRandom.current().nextInt(list.size());
      bIndex = ThreadLocalRandom.current().nextInt(list.size());
    }

    String aId = list.get(aIndex);
    String bId = list.get(bIndex);
    return Tuple2.of(aId, bId);
  }

  @Test
  public void testDeadLock() throws Exception {
    init();
    ExecutorService pool = Executors.newFixedThreadPool(6);

    for (int i = 0; i < 100; i++) {
      pool.submit(() -> {
        Tuple2<String, String> playerPair = this.randomPlayer();
        String aId = playerPair.getFirst();
        String bId = playerPair.getSecond();
        Player a = playerMap.get(aId);
        Player b = playerMap.get(bId);

        log.debug("{} {} prepare enter arena", aId, bId);

        boolean enter = false;
        try {
          a.getLock().lock();
          b.getLock().lock();

          enter = true;
          log.info("enter ids: a={} b={}", aId, bId);
          arenaLogic(a, b);
        } catch (Exception e) {
          log.error("", e);
        } finally {
          if (enter) {
            a.getLock().unlock();
            b.getLock().unlock();
            log.info("{} {} exit arena", aId, bId);
          }
        }
      });
      Thread.sleep(10);
    }
    log.warn("end loop");

    Thread.currentThread().join(Duration.ofMinutes(10).toMillis());
  }

  /**
   * 保证获取锁时的顺序，释放锁的顺序无所谓
   */
  @Test
  public void testSortLock() throws Exception {
    init();
    ExecutorService pool = Executors.newFixedThreadPool(6);

    for (int i = 0; i < 100000; i++) {
      pool.submit(() -> {
        Tuple2<String, String> playerPair = this.randomPlayer();
        String aId = playerPair.getFirst();
        String bId = playerPair.getSecond();

        if (aId.compareTo(bId) > 0) {
          String temp = aId;
          aId = bId;
          bId = temp;
        }

        Player a = playerMap.get(aId);
        Player b = playerMap.get(bId);

        log.debug("{} {} prepare enter arena", aId, bId);

        boolean enter = false;
        try {
          a.getLock().lock();
          b.getLock().lock();

          enter = true;
          log.info("enter ids: a={} b={}", aId, bId);
          arenaLogic(a, b);
        } catch (Exception e) {
          log.error("", e);
        } finally {
          if (enter) {
            a.getLock().unlock();
            b.getLock().unlock();
            log.info("{} {} exit arena", aId, bId);
          }
        }
      });
      Thread.sleep(10);
    }
    log.warn("end loop");

    Thread.currentThread().join(Duration.ofMinutes(10).toMillis());
  }

  @Test
  public void testTryThenLock() throws Exception {
    init();
    ExecutorService pool = Executors.newFixedThreadPool(6);

    for (int i = 0; i < 1000; i++) {
      pool.submit(() -> {
        Tuple2<String, String> playerPair = this.randomPlayer();
        String aId = playerPair.getFirst();
        String bId = playerPair.getSecond();
        Player a = playerMap.get(aId);
        Player b = playerMap.get(bId);

        log.debug("{} {} prepare enter arena", aId, bId);

        boolean enter = false;
        try {

          if (a.getLock().tryLock() && b.getLock().tryLock()) {
            a.getLock().lock();
            b.getLock().lock();
            enter = true;
            log.info("enter ids: a={} b={}", aId, bId);
            arenaLogic(a, b);
          } else {
            log.warn("failed get lock");
          }
        } catch (Exception e) {
          log.error("", e);
        } finally {
          if (enter) {
            a.getLock().unlock();
            b.getLock().unlock();
            log.info("{} {} exit arena", aId, bId);
          }
        }
      });
      Thread.sleep(10);
    }

    log.warn("end loop");
    Thread.currentThread().join(Duration.ofMinutes(10).toMillis());
  }
}
