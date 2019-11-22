package com.github.kuangcp.wrapper.consumer;

import com.github.kuangcp.wrapper.Message;

/**
 * @author https://github.com/kuangcp on 2019-11-13 09:49
 */
public interface SingleTopicMessageExecutor<T> extends MessageExecutor<Message<T>>, SingleTopic {

}
