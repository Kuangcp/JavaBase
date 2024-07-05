package thread.jmx;

import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * @author kuangcp on 2019-04-19 10:08 AM
 */
@Slf4j
public class ThreadInfoMxBean {

    public static void main(String[] args) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] infos = bean.dumpAllThreads(false, false);
        for (ThreadInfo info : infos) {
            log.info("id={} name={}", info.getThreadId(), info.getThreadName());
        }
    }

    // 预期输出
    //name=Monitor Ctrl-Break
    //name=Signal Dispatcher  分发处理发送给JVM进程信号
    //name=Finalizer  调用对象 finalize()
    //name=Reference Handler 清除 Reference
    //name=main
}