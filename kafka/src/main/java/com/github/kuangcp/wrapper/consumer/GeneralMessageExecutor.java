package com.github.kuangcp.wrapper.consumer;

import com.github.kuangcp.wrapper.Message;

/**
 * @author https://github.com/kuangcp on 2019-11-13 09:49
 */
public interface GeneralMessageExecutor<T> extends MessageTopic, MessageExecutor<Message<T>> {

  Class<T> getContentClass();
}
