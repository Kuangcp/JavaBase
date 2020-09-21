package redis.migration;

import static java.util.stream.Collectors.groupingBy;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

/**
 * @author kuangcp on 18-7-17-上午10:42
 */
@Slf4j
public class Main {

  private final RedisPools origin = new RedisPools();
  private final RedisPools target = new RedisPools();

  private final RedisPoolProperty originProperty;
  private final RedisPoolProperty targetProperty;

  private final Integer originDatabase;
  private final Integer targetDatabase;

  Main(RedisPoolProperty originProperty, RedisPoolProperty targetProperty,
      Integer originDatabase, Integer targetDatabase) {
    this.originProperty = originProperty;
    this.targetProperty = targetProperty;
    this.originDatabase = originDatabase;
    this.targetDatabase = targetDatabase;
  }

  /**
   * Jedis对象 并不是并发安全的，所以应该使用JedisPool
   */
  void transferAllKey() throws InterruptedException {
    boolean init = initRedisPool();
    if (!init) {
      return;
    }

    JedisPool originPool = origin.getJedisPool();
    JedisPool targetPool = target.getJedisPool();

    try (final Jedis resource = originPool.getResource()) {
      if (!RedisPools.isAvailable(resource)) {
        log.error("conn has invalid ");
        return;
      }
    }

    try (final Jedis resource = targetPool.getResource()) {
      if (!RedisPools.isAvailable(resource)) {
        log.error("conn has invalid ");
        return;
      }
    }

    Set<String> keys = getKeys(origin, originDatabase);
    ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    final CountDownLatch latch = new CountDownLatch(keys.size());
    Map<Integer, List<String>> collect = keys.stream().collect(groupingBy(String::length));
    for (List<String> value : collect.values()) {
      for (String k : value) {
        pool.submit(() -> {
          transferOneKey(k, originPool, targetPool);
          latch.countDown();
        });
      }
    }
    latch.await();
  }

  private boolean initRedisPool() {
    if (Objects.isNull(originProperty) || Objects.isNull(targetProperty)) {
      log.warn("init property error: ");
      return false;
    }

    origin.initPool(originProperty);
    target.initPool(targetProperty);

    return true;
  }

  private void transferOneKey(String key, JedisPool originPool, JedisPool targetPool) {
    try (Jedis originJedis = originPool.getResource()) {
      originJedis.select(originDatabase);
      String type = originJedis.type(key);
      Optional<RedisDataType> dataType = RedisDataType.of(type);

      if (!dataType.isPresent()) {
        log.warn("unsupported data type: key={} type={}", key, type);
        return;
      }

      log.info("prepared : key={} type={}", key, type);

      try (final Jedis targetJedis = targetPool.getResource()) {
        targetJedis.select(targetDatabase);
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
        log.info("transferOneKey : key={} stamp={}", key, System.currentTimeMillis());
      }
    }
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
  private Set<String> getKeys(RedisPools pool, int database) {
    final JedisPool jedisPool = pool.getJedisPool();
    try (final Jedis jedis = jedisPool.getResource()) {
      if (Objects.isNull(jedis)) {
        return Collections.emptySet();
      }
      jedis.select(database);
      return jedis.keys("*");
    }
  }
}
