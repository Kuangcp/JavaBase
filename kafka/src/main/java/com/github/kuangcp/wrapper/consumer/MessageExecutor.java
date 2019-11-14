package com.github.kuangcp.wrapper.consumer;

import com.github.kuangcp.wrapper.Message;

/**
 * @author https://github.com/kuangcp on 2019-11-13 09:47
 */
public interface MessageExecutor {

  /**
   * 处理消息
   */
  void execute(Message message);
}
