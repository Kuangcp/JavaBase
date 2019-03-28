package com.github.kuangcp.exception.service;

import com.github.kuangcp.exception.ServiceMapping;

/**
 * 业务逻辑的总的接口
 *
 * @author Administrator
 */
public interface Service {

  Object service(ServiceMapping serviceMapping) throws Exception;
}
