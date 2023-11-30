package com.github.kuangcp.exit;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2023-12-01 01:16
 */
public class TrapSignal {

    public static void main(String[] args) throws InterruptedException {
        // 同样地，如果使用了 -Xrs JVM参数，以下信号量的监听也会报错
        DebugSignalHandler.listenTo("HUP");
//        DebugSignalHandler.listenTo("INT");
//        DebugSignalHandler.listenTo("TERM");


        // 注意此信号无法监听，报错： Signal already used by VM or OS: SIGKILL
//        DebugSignalHandler.listenTo("KILL");

        while (true) {
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
