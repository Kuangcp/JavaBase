package com.github.kuangcp.wrapper.consumer;

import com.github.kuangcp.wrapper.domain.Topics;
import lombok.extern.slf4j.Slf4j;

/**
 * @author https://github.com/kuangcp on 2019-11-22 21:16
 */
@Slf4j
public class HiExecutor implements SimpleMessageExecutor<String> {

  @Override
  public void execute(String message) {
    log.info(": message={}", message);
  }

  @Override
  public String getTopic() {
    return Topics.HI;
  }
}
