package com.github.kuangcp.proxy.dao.jdkproxy;

import com.github.kuangcp.proxy.dao.common.InterceptorLogic;
import com.github.kuangcp.proxy.dao.common.Permission;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 动态增强，织入权限控制
 *
 * @author https://github.com/kuangcp on 2019-09-29 01:33
 */
@Slf4j
@AllArgsConstructor
public class PermissionInterceptor implements InvocationHandler {

  private Permission permission;
  private Object target;

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    log.debug("method={} proxy={}", method.getName(), proxy);

    return InterceptorLogic.permissionLogic(permission, target, method, args);
  }
}
