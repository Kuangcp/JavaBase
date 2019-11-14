package com.github.kuangcp.wrapper.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author https://github.com/kuangcp on 2019-11-13 09:44
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaProducerConfig {

  //metadata.broker.list
  //broker列表可以为kafka server的子集,因为producer需要从broker中获取metadata
  //尽管每个broker都可以提供metadata,此处还是建议,将所有broker都列举出来
  private String kafkaBrokerIpAndPortList;

  //producer.type
  //消息发送的模式，同步或异步，异步的时候消息将会在本地buffer，并适时批量发送
  //default sync
  private String producerType;

  //batch.num.messages
  //消息在producer端buffer的条数，仅在producer.type=async下有效
  //default 200
  private String batchNumMessages;

  //request.required.acks
  //producer接收消息ack的时机，默认为0
  // 0：produce人不会等待broker发送ack
  // 1：当leader接收到消息后发送ack
  // 2：当所有的follower都同步消息成功后发送ack
  private String requestRequiredAcks;

  //serializer.class
  //消息序列化类
  //向kafka发送数据，默认支持String和byte[]2种类型,分别支持String和二进制
  //包括kafka.serializer.StringEncoder和kafka.serializer.DefaultEncoder 2个类
  //default kafka.serializer.DefaultEncoder
  private String serializerClass;
  //key.serializer.class
  //default =serializer.class =kafka.serializer.StringEncoder
  private String keySerializerClass;

  //compression.codec
  //消息压缩算法，none，gzip，snappy
  //default none
  private String compressionCodec;
  //compressed.topics
  //default null
  private String compressedTopics;

  //partitioner.class
  //partitions路由类，消息在发送时将根据此实例的方法获得partition索引号
  //default kafka.producer.DefaultPartitioner
  private String partitionerClass;


  //message.send.max.retries
  //default 3
  private String messageSendMaxRetries;

  //retry.backoff.ms
  //default 100
  private String retryBackoff;

  //topic.metadata.refresh.interval.ms
  //default 600000
  private String topicMetadataRefreshInterval;

  //---还有更多配置，详见ProducerConfig
}
