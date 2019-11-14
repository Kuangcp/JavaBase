package com.github.kuangcp.wrapper.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author https://github.com/kuangcp on 2019-11-13 09:43
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaConsumerConfig {

  //group.id
  private String groupId;

  //zookeeper.connect
  //zookeeper server list
  private String zookeeperServerIpAndPortList;

  //zookeeper.session.timeout.ms
  // default 6000
  private String zookeeperSessionTimeout;
  //zookeeper.connection.timeout.ms
  //timeout in ms for connecting to zookeeper
  // default =zookeeperSessionTimeout =6000
  private String zookeeperConnectionTimeout;
  //zookeeper.sync.time.ms
  // default 2000
  private String zookeeperSyncTime;

  //consumer.timeout.ms
  // default -1
  private String consumerTimeout;

  //auto.commit.enable
  //如果设为true，consumer会定时向ZooKeeper发送已经获取到的消息的offset
  // 当consumer进程挂掉时，已经提交的offset可以继续使用，让新的consumer继续工作
  // 如果为false，会重复读取挂掉的consumer已经读取过的消息
  //default true
  private String autoCommitEnable;

  //auto.commit.interval.ms
  //consumer向ZooKeeper发送offset的时间间隔
  //default 60000
  private String autoCommitInterval;

  //rebalance.backoff.ms
  //该设置过短就会导致old consumer还没有来得及释放资源，new consumer重试失败多次到达阀值就退出了。
  // 抛出 ConsumerRebalanceFailedException
  //所以要确保rebalanceMaxRetries * rebalanceMaxRetries > zookeeperSessionTimeout
  // default =zookeeperSyncTime =2000
  private String reBalanceBackOff;
  //rebalance.max.retries
  // default 4
  private String rebalanceMaxRetries;

  //---还有更多配置，详见ConsumerConfig，

}
