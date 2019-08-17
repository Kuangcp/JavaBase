package com.github.kuangcp.bloom.filter;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-08-04 16:34
 */
@Slf4j
public class BloomFilterTest {

  @Test
  public void testMain() {
    BloomFilter bloomFilter = new BloomFilter();
    int count = 0;
    for (int i = 0; i < 10; i++) {
      String str = UUID.randomUUID().toString();
      boolean exist = bloomFilter.exist(str);
      if (exist) {
        System.out.println("already exist");
        count++;
      }

      bloomFilter.add(str);

      exist = bloomFilter.exist(str);
      if (!exist) {
        System.out.println("not exist");
        count++;
      }
    }
    log.info("invalid: count={}", count);
  }
}
