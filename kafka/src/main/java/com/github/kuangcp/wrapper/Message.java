package com.github.kuangcp.wrapper;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author https://github.com/kuangcp on 2019-11-13 09:48
 */
@Data
@NoArgsConstructor
public class Message<T> implements Serializable {

  private static final long serialVersionUID = 0L;

  /**
   * 消息Id
   */
  private String id = UUID.randomUUID().toString();

  /**
   * 消息创建时间
   */
  private Date createTime = new Date();

  /**
   * 消息内容
   */
  private T content;

  public Message(T content) {
    this.content = content;
  }
}