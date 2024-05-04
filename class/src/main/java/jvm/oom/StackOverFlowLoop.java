package jvm.oom;

import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2024-05-04 21:04
 */
@Slf4j
public class StackOverFlowLoop {

    /**
     * MaxJavaStackTraceDepth 的值为1024 也就是栈溢出时报错信息里打印出栈的深度
     */
    public static void main(String[] args) {
        try {
            main(args);
        } catch (Throwable e) {
            // 此时因为try中触发了栈溢出，栈帧回溯，继续进入main方法，又逐步积累栈到栈溢出，再回溯，周而复始
            main(args);
        }
    }
}
