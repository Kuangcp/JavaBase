package com.github.kuangcp.math;

import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a>
 * 2019-06-16 11:19
 */
@Slf4j
class Martingale {

  /**
   * 一种赌博方式, 如果输了就加倍投入, 初始本金为1
   * 乍一看, 只要赢了一次, 前面的投入就都回本了, 且盈利 1, 但是前提是具有足量的资金
   * 但是赌场的资金是远比你的资金雄厚的, 所以赌徒久赌必输, 这还是建立在胜率5成的基础上
   *
   * @param principal 本金
   * @param count 赌博次数
   * @return 剩余本金
   */
  long martingale(long principal, int count) {
    if (count < 0) {
      return principal;
    }
    long origin = principal;
    long start = 1L;
    for (int i = 0; i < count; i++) {
      if (principal < start) {
        log.warn("本金不足: principal={} except={}", principal, start);
        return principal;
      }

      principal -= start;
      int temp = ThreadLocalRandom.current().nextInt(0, 100);
      // 假定输的概率 五成
      if (temp > 50) {
        log.info("亏损:{} 当前变动:{}", start, (principal - origin));
        start *= 2;
      } else {
        log.info("盈利:{} 当前变动:{}", start, (principal - origin));
        principal += start * 2;
      }
    }
    return principal;
  }
}
