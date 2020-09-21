package redis.migration;

import java.util.List;
import java.util.Objects;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by https://github.com/kuangcp on 17-6-9  上午9:56 一个连接redis的连接池实例, TODO 需要检查性能
 */
@Slf4j
@Data
public class RedisPools {

  private static Logger logger = LoggerFactory.getLogger(RedisPools.class);
  private JedisPool jedisPool;
  private RedisPoolProperty property;

  /**
   * 初始化Redis连接池
   */
  protected void initPool() {
    if (Objects.isNull(property) || !property.isAbleToInit()) {
      return;
    }

    try {
      JedisPoolConfig config = new JedisPoolConfig();
      if (Objects.nonNull(property.getMaxActive())) {
        config.setMaxTotal(property.getMaxActive());
      }
      if (Objects.nonNull(property.getMaxIdle())) {
        config.setMaxIdle(property.getMaxIdle());
      }
      if (Objects.nonNull(property.getMaxWaitMills())) {
        config.setMaxWaitMillis(property.getMaxWaitMills());
      }
      config.setTestOnBorrow(property.isTestOnBorrow());

      log.trace("password: " + property);

      if (StringUtils.isNoneBlank(property.getPassword())) {
        jedisPool = new JedisPool(config, property.getHost(), property.getPort(),
            property.getTimeout(), property.getPassword());
      } else {
        jedisPool = new JedisPool(config, property.getHost(), property.getPort(),
            property.getTimeout());
      }
    } catch (Exception e) {
      logger.error("create pool error", e);
    }
  }

  /**
   * 初始化连接池
   *
   * @param property 属性对象
   */
  protected void initPool(RedisPoolProperty property) {
    setProperty(property);
    initPool();
  }

  public JedisPool getJedisPool() {
    if (jedisPool == null) {
      logger.info("pool is empty re init");
      initPool();
    }
    return jedisPool;
  }

  /**
   * 测试当前连接池是否可用
   */
  public static boolean isAvailable(Jedis jedis) {
    if (Objects.isNull(jedis)) {
      return false;
    }
    String pingResult = jedis.ping();
    return "pong".equalsIgnoreCase(pingResult);
  }

  /**
   * 得到最大的数据库数
   *
   * @return 整型值
   */
  public Integer getDatabaseNum() {
    List config = jedisPool.getResource().configGet("*");
    return Integer.parseInt(config.get(config.indexOf("databases") + 1).toString());
  }

  /**
   * 销毁连接池，有用的
   *
   * @return 销毁结果
   */
  public boolean destroyPool() {
    logger.debug("destroy pool: property={}", this.getProperty());
    if (jedisPool != null) {
      jedisPool.destroy();
      return true;
    }
    return false;
  }
}