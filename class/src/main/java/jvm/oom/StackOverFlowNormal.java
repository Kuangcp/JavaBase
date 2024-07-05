package jvm.oom;

import lombok.extern.slf4j.Slf4j;

/**
 * 1. -Xss240k 设置栈大小, 所以设置的越大, 递归次数越大
 * 2. 本地变量越多 栈深度越小
 * <p>
 * Java8 至少需要 228k 否则会报错无法运行, 默认值为 1024k
 *
 * @author kuangcp on 4/3/19-10:47 PM
 */
@Slf4j
public class StackOverFlowNormal {

    private int count = 1;

    private void stackLeak() {
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        int f = 0;
        String g = null;

        count++;
        stackLeak();
    }

    public static void main(String[] args) {
        StackOverFlowNormal overFlow = new StackOverFlowNormal();
        try {
            overFlow.stackLeak();
        } catch (Throwable e) {
            log.info("{} count={}", e.getClass(), overFlow.count);
        }
    }
}
