package com.github.kuangcp.bloom.filter;

import java.util.Objects;
import java.util.function.Function;
import org.apache.commons.codec.digest.MurmurHash2;

/**
 * https://stackoverflow.com/questions/34595/what-is-a-good-hash-function
 *
 * @author https://github.com/kuangcp on 2019-08-04 16:02
 */
public class HashFunctions {

  static Function<String, Integer> hashByObjects = url -> {
    int hash = Objects.hash(url);

    hash ^= hash << 3;
    hash += hash >> 5;
    hash ^= hash << 4;
    hash += hash >> 17;
    hash ^= hash << 25;
    hash += hash >> 6;

    return hash > 0 ? hash : hash * -1;
  };

  static Function<String, Integer> hashWithString = url -> {
    if (Objects.isNull(url)) {
      return 0;
    }
    int hash = 7;
    for (int i = 0; i < url.length(); i++) {
      hash = hash * 31 + url.charAt(i);
    }
    return hash > 0 ? hash : -hash;
  };

  // Kafka 默认分区器 hash 算法
  static Function<String, Integer> murmurHash2 = MurmurHash2::hash32;

}
