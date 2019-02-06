package redis.migration;

import java.util.Arrays;
import java.util.Optional;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 2/6/19-6:01 PM
 */

public enum RedisDataType {
  String, ZSET, LIST, HASH, SET;

  public boolean equals(String type) {
    return this.name().equalsIgnoreCase(type);
  }

  public static Optional<RedisDataType> of(String type) {
    return Arrays.stream(values()).filter(t -> t.equals(type)).findAny();
  }
}
