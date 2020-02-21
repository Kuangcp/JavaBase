package com.github.kuangcp.exception;

import lombok.Data;

/**
 * 只是一个数据格式而已，作为各个类通信的数据格式，指明某类某方法要执行
 *
 * @author Myth
 */
@Data
public class ServiceMapping {

  private String serviceClass;//封装service的全名
  private String method;//某一个service的方法
}
