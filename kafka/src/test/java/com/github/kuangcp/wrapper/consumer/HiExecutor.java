package com.github.kuangcp.wrapper.consumer;

/**
 * @author https://github.com/kuangcp on 2019-11-22 21:16
 */
public class HiExecutor implements SimpleTopicMessageExecutor {

  @Override
  public void execute(String message) {
    System.out.println(message);
  }

  @Override
  public String getTopic() {
    return "Hi";
  }
}
