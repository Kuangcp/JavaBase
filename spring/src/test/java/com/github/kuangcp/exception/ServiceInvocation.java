package com.github.kuangcp.exception;


import com.github.kuangcp.exception.service.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceInvocation {

  /**
   * 方法调用的必经路径
   * 该方法是service总的调用接口,所以能在这里统一处理异常
   */
  public static Object execution(ServiceMapping serviceMapping) {
    Object obj = null;
    try {
      Service service = (Service) Class.forName(serviceMapping.getServiceClass()).newInstance();
      obj = service.service(serviceMapping);
    } catch (Exception e) {
      log.error("", e);
    }
    return obj;
  }
}
