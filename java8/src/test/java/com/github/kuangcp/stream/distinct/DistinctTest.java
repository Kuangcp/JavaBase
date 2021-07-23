package com.github.kuangcp.stream.distinct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp
 */
@Slf4j
public class DistinctTest {

  // 如果使用 Data注解会使用到 EqualsAndHashCode 注解（使用值计算hashCode和equals）
//  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  static class ValElement {

    String name;
    BigDecimal val;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public BigDecimal getVal() {
      return val;
    }

    public void setVal(BigDecimal val) {
      this.val = val;
    }

    @Override
    public String toString() {
      return "ValElement{" +
          "name='" + name + '\'' +
          ", val=" + val +
          '}';
    }
  }

  @Test
  public void testDistinct() {
    List<ValElement> cache = new ArrayList<>();
    ValElement a = ValElement.builder().name("a").val(BigDecimal.ONE).build();
    ValElement b = ValElement.builder().name("b").val(BigDecimal.TEN).build();
    ValElement c = ValElement.builder().name("c").val(BigDecimal.valueOf(100)).build();

    cache.add(a);
    cache.add(a);
    cache.add(a);
    cache.add(b);
    cache.add(b);
    cache.add(b);
    cache.add(c);
    cache.add(c);
    cache.add(c);
    cache.add(c);

    cache.stream().distinct().forEach(v -> {
      BigDecimal old = v.getVal();
      BigDecimal newVal = old.multiply(BigDecimal.valueOf(3));
      log.info("old={} new={} v={} code={}", old, newVal, v, v.hashCode());
      v.setVal(newVal);
    });

    cache.forEach(v -> log.info("v={}", v));
  }
}
