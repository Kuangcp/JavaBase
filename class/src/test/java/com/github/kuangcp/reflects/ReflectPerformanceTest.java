package com.github.kuangcp.reflects;

import com.github.kuangcp.time.GetRunTime;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * 反射的性能问题 http://www.cnblogs.com/zhishan/p/3195771.html
 * https://cloud.tencent.com/developer/article/2180451
 * cglib(已缓存) 耗时 50%-70% 于缓存, 10% 于 原始方式
 * TODO 操作字节码方式取代反射
 * TODO jmh
 *
 * @author kuangcp
 */
@Slf4j
public class ReflectPerformanceTest {

    private static final int LOOP_SIZE = 5000_000;
    private static final GetRunTime time = new GetRunTime();

    // primitive method invoke
    @Test
    public void testGetSet() {
        long sum = 0;
        TargetObject targetObject = new TargetObject();

        time.startCount();
        for (int i = 0; i < LOOP_SIZE; ++i) {
            targetObject.setNum(i);
            sum += targetObject.getNum();
        }
        time.endCountOneLine("invoke get-set method ");
        log.info("LOOP_SIZE {} 和是 {} ", LOOP_SIZE, sum);
    }

    @Test
    public void testOriginReflect() throws Exception {
        long sum = 0;
        TargetObject targetObject = new TargetObject();

        time.startCount();
        for (int i = 0; i < LOOP_SIZE; ++i) {
            Method method = targetObject.getClass().getMethod("setNum", int.class);
            method.invoke(targetObject, i);
            sum += targetObject.getNum();
        }

        time.endCountOneLine("simple reflect ");
        log.info("LOOP_SIZE {} 和是 {} ", LOOP_SIZE, sum);
    }

    @Test
    public void testOriginReflectWithCache() throws Exception {
        long sum = 0;
        TargetObject targetObject = new TargetObject();
        time.startCount();

        Method method = targetObject.getClass().getMethod("setNum", int.class);
        for (int i = 0; i < LOOP_SIZE; ++i) {
            method.invoke(targetObject, i);
            int num = targetObject.getNum();
            sum += num;
        }

        time.endCountOneLine("simple reflect with cache");
        log.info("LOOP_SIZE {} 和是 {} ", LOOP_SIZE, sum);
    }

    @Test
    public void testCglib() throws Exception {
        long sum = 0;
        TargetObject targetObject = new TargetObject();

        // cglib 3.2.4
        FastClass testClazz = FastClass.create(TargetObject.class);
        FastMethod method = testClazz.getMethod("setNum", new Class[]{int.class});

        Object[] param = new Object[1];
        time.startCount();
        for (int i = 0; i < LOOP_SIZE; ++i) {
            param[0] = i;
            method.invoke(targetObject, param);
            sum += targetObject.getNum();
        }

        time.endCountOneLine("use cglib ");
        log.info("LOOP_SIZE {} 和是 {} ", LOOP_SIZE, sum);
    }

}
