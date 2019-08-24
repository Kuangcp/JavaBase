package com.github.kuangcp.bloom.filter;

import com.github.kuangcp.time.GetRunTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-08-04 16:34
 */
@Slf4j
public class BloomFilterTest {

  @Test
  public void testMain() {
    GetRunTime run = new GetRunTime();
    BloomFilter bloomFilter = new BloomFilter();
    int count = 0;
    int scale = 10000000;
    run.startCount();
    for (int i = 0; i < scale; i++) {
      String str = String.valueOf(i);
      boolean exist = bloomFilter.exist(str);
      if (exist) {
        System.out.println("already exist: " + str);
        count++;
      }

      bloomFilter.add(str);

      exist = bloomFilter.exist(str);
      if (!exist) {
        System.out.println("should exist: "+str);
        count++;
      }
    }
    run.endCountOneLine("end");
    log.info("invalid: count={}", count);
  }
}
