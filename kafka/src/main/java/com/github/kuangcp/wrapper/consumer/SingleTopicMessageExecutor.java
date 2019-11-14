package com.github.kuangcp.wrapper.consumer;

/**
 * @author https://github.com/kuangcp on 2019-11-13 09:49
 */
public interface SingleTopicMessageExecutor extends MessageExecutor {

  String getTopic();
}
