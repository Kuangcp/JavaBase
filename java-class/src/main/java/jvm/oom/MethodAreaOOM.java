package jvm.oom;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * @author kuangcp on 4/5/19-12:56 AM
 */
public class MethodAreaOOM {

  public static void main(String[] args) {
    while (true) {
      Enhancer enhancer = new Enhancer();
      enhancer.setSuperclass(OOMObject.class);
      enhancer.setUseCache(false);
      enhancer.setCallback((MethodInterceptor) (obj, method, args1, proxy) ->
          proxy.invokeSuper(obj, args)
      );
      enhancer.create();
    }
  }

  private static class OOMObject {

  }
}
