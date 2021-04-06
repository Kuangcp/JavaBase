package com.github.kuangcp.bloom.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;

/**
 * https://blog.csdn.net/hguisu/article/details/7866173
 *
 * @author https://github.com/kuangcp on 2019-08-04 15:57
 */
@Slf4j
public class BloomFilter {

  private static final List<Function<String, Integer>> functions = new ArrayList<>();
  private static final short LEN_OF_BYTE = 7;

  private final byte[] cache = new byte[2 << 28];

  static {
    functions.add(HashFunctions.hashByObjects);
    functions.add(HashFunctions.hashWithString);
    functions.add(HashFunctions.murmurHash2);
  }

  public void add(String str) {
    for (Function<String, Integer> function : functions) {
      Integer hash = function.apply(str);
      int index = hash / LEN_OF_BYTE;
      if (log.isDebugEnabled()) {
        log.debug("hash: index={}", index);
      }
      byte unitByte = cache[index];
      byte unitIndex = (byte) (hash % LEN_OF_BYTE);
      if (log.isDebugEnabled()) {
        log.debug("read value: byte={} unitIndex={}", toStr(unitByte), unitIndex);
      }
      unitByte = (byte) (unitByte | 1 << (unitIndex));
      cache[index] = unitByte;
      if (log.isDebugEnabled()) {
        log.debug("set value:  byte={}", toStr(unitByte));
        log.debug("");
      }
    }
  }

  public boolean exist(String str) {
    Predicate<Function<String, Integer>> has = func -> {
      Integer hash = func.apply(str);
      int index = hash / LEN_OF_BYTE;
      byte unitByte = cache[index];
      byte unitIndex = (byte) (hash % LEN_OF_BYTE);
      return (unitByte & 1 << (unitIndex)) > 0;
    };

    return functions.stream().allMatch(has);
  }

  private String toStr(byte value) {
    return Integer.toBinaryString(value);
  }
}
