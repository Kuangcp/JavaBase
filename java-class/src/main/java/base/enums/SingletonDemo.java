package base.enums;

import lombok.extern.slf4j.Slf4j;

/**
 * created by https://gitee.com/gin9
 * use enum create singleton
 * @author kuangcp on 2/17/19-9:22 AM
 */
@Slf4j
public enum SingletonDemo {

  INSTANCE;

  public void doA() {
    log.info("invoke doA");
  }

}
