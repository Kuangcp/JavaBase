package com.github.kuangcp.bloom.filter;

import java.util.Objects;
import java.util.function.Function;

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

}
