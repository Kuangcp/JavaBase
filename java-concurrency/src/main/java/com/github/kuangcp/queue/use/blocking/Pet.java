package com.github.kuangcp.queue.use.blocking;

import lombok.Data;

/**
 * Created by https://github.com/kuangcp on 17-8-18  上午9:28
 * 展示BlockingQueue的特性 多个兽医治疗宠物的队列
 */
@Data
abstract class Pet {

  protected String name;

  Pet(String name) {
    this.name = name;
  }

  abstract void examine();
}