package com.github.kuangcp.exception;


import com.github.kuangcp.exception.service.Service;

public class ServiceInvocation {

  /**
   * 方法调用的必经路径
   * 该方法是service总的调用接口,所以能在这里统一处理异常
   */
  public static Object execution(ServiceMapping serviceMapping) {
    /**
     * serviceMapping
     *   serviceClass cn.itcast.exception.service.StudentServiceImpl
     *   methodName   savePerson
     */
    Object obj = null;
    try {
      Service service = (Service) Class.forName(serviceMapping.getServiceClass()).newInstance();
      obj = service.service(serviceMapping);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return obj;
  }
}
