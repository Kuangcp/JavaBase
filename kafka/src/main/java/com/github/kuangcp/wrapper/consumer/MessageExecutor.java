package com.github.kuangcp.wrapper.consumer;

/**
 * @author https://github.com/kuangcp on 2019-11-13 09:47
 */
public interface MessageExecutor<T> {

  /**
   * 处理消息
   */
  void execute(T message);
}
