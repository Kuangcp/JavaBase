package redis.migration;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;


/**
 * Created by https://github.com/kuangcp on 17-6-9  下午9:09
 *
 * @author kcp RedisPool 的必要的非必要的所有连接属性
 */
@Data
public class RedisPoolProperty {

  //最大连接数
  private Integer maxActive;
  //最大闲置数,超过的就会被回收掉
  private Integer maxIdle;
  //获取连接等待的最长时间，超过就报异常
  private Integer maxWaitMills;
  //获取连接前是否测试，true就保证了获取到的每个连接是可用的，当然不一定获取的到
  private boolean testOnBorrow = true;
  private int port;
  //读取超时,是否是配置文件中的那个
  private int timeout;
  private String host;
  private String name;
  private String poolId;
  //设定默认值为空字符串不能为null
  private String password = "";


  // host and port is effective
  public boolean isAbleToInit() {
    return StringUtils.isNoneBlank(host) && port > 0 && port < 65535 && timeout > 0;
  }
}
