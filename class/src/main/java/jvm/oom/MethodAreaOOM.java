package jvm.oom;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * -XX:MaxMetaSpaceSize=20M
 * <p>
 * MetaSpace 一直缓慢的上涨, 一直在载入新的类,
 * 在Jvisualvm中看到的元空间最大是好几个g, 但是metaspace上涨到设置的最大限制时会抛出OOM
 * <p>
 * Caused by: java.lang.OutOfMemoryError: Metaspace
 *
 * @author kuangcp on 4/5/19-12:56 AM
 */
@Slf4j
public class MethodAreaOOM {

    // 使用 cglib 不停创建 OOMObject 的子类
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

    // 也可以通过 javassist
    // static javassist.ClassPool cp = javassist.ClassPool.getDefault();
    //  for (int i = 0; ; i++) {
    //    Class c = cp.makeClass("eu.plumbr.demo.Generated" + i).toClass();
    //  }

    private static class OOMObject {

        public OOMObject() {
        }
    }
}
