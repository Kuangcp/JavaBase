package com.github.kuangcp.random;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2020-03-20 09:58
 */
public class MT19937RandomTest {

  @Test
  public void testRandom() {
    int maxSize = 100000;
    Set<Integer> pool = new HashSet<>();
    for (int i = 0; i < maxSize; i++) {
      MT19937Random random = new MT19937Random(System.nanoTime());
      pool.add(random.next());
    }

    assertThat(pool.size(), equalTo(maxSize));
  }
}
