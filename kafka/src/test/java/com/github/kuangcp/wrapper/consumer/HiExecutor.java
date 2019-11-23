package com.github.kuangcp.wrapper.consumer;

import com.github.kuangcp.wrapper.domain.Topics;

/**
 * @author https://github.com/kuangcp on 2019-11-22 21:16
 */
public class HiExecutor implements SimpleMessageExecutor<String> {

  @Override
  public void execute(String message) {
    System.out.println(message);
  }

  @Override
  public String getTopic() {
    return Topics.USER_LOGIN;
  }
}
