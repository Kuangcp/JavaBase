package redis.migration;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

/**
 * @author kuangcp on 18-7-17-上午10:42
 */
@Slf4j
public class Main {

  private RedisPools origin = new RedisPools();
  private RedisPools target = new RedisPools();

  private RedisPoolProperty originProperty;
  private RedisPoolProperty targetProperty;
  private Integer originDatabase;
  private Integer targetDatabase;

  public Main(RedisPoolProperty originProperty, RedisPoolProperty targetProperty,
      Integer originDatabase, Integer targetDatabase) {
    this.originProperty = originProperty;
    this.targetProperty = targetProperty;
    this.originDatabase = originDatabase;
    this.targetDatabase = targetDatabase;
  }

  /**
   *
   */
  public void transferAllKey() {
    boolean init = init();
    if (!init) {
      return;
    }

    if (!origin.isAvailable() || !target.isAvailable()) {
      log.error("conn has invalid ");
      return;
    }

    getKeys(origin, originProperty, originDatabase)
        .forEach(this::transfer);
  }

  private boolean init() {
    if (Objects.isNull(originProperty) || Objects.isNull(targetProperty)) {
      log.warn("init property error: ");
      return false;
    }

    origin.initPool(originProperty);
    target.initPool(targetProperty);

    return true;
  }

  private void transfer(String key) {
    Optional<Jedis> originOpt = origin.getJedis();
    Optional<Jedis> targetOpt = target.getJedis();

    if (!originOpt.isPresent() || !targetOpt.isPresent()) {
      log.warn("connect failed: ");
      return;
    }
    Jedis originJedis = originOpt.get();
    Jedis targetJedis = targetOpt.get();
    originJedis.select(originDatabase);
    targetJedis.select(targetDatabase);

    String type = originJedis.type(key);

    Optional<RedisDataType> dataType = RedisDataType.of(type);
    if (!dataType.isPresent()) {
      log.warn("unsupported data type: type={}", type);
      return;
    }

    switch (dataType.get()) {
      case SET:
        transferSet(key, originJedis, targetJedis);
        break;
      case HASH:
        transferHash(key, originJedis, targetJedis);
        break;
      case LIST:
        transferList(key, originJedis, targetJedis);
        break;
      case ZSET:
        transferZSet(key, originJedis, targetJedis);
        break;
      case String:
        targetJedis.set(key, originJedis.get(key));
        break;
    }
    log.info("transfer : key={}", key);

//    log.warn("transfer failed: key={} type={}", key, type);

  }

  private void transferZSet(String key, Jedis originJedis, Jedis targetJedis) {
    Set<Tuple> tuples = originJedis.zrangeWithScores(key, 0, -1);
    Map<String, Double> collect = tuples.stream()
        .collect(Collectors.toMap(Tuple::getElement, Tuple::getScore));
    targetJedis.zadd(key, collect);
  }

  private void transferList(String key, Jedis originJedis, Jedis targetJedis) {
    List<String> data = originJedis.lrange(key, 0, -1);
    String[] temps = new String[data.size()];
    String[] results = data.toArray(temps);
    targetJedis.lpush(key, results);
  }

  private void transferHash(String key, Jedis originJedis, Jedis targetJedis) {
    Map<String, String> tempData = originJedis.hgetAll(key);
    targetJedis.hset(key, tempData);
  }

  private void transferSet(String key, Jedis originJedis, Jedis targetJedis) {
    Set<String> members = originJedis.smembers(key);
    String[] temp = new String[members.size()];
    String[] result = members.toArray(temp);
    targetJedis.sadd(key, result);
  }

  /**
   * get keys
   */
  private Set<String> getKeys(RedisPools pool, RedisPoolProperty property, int database) {
    Optional<Jedis> jedisOpt = pool.getJedis(property);
    if (!jedisOpt.isPresent()) {
      return new HashSet<>(0);
    }

    Jedis jedis = jedisOpt.get();
    jedis.select(database);
    return jedis.keys("*");
  }

}
