package com.github.kuangcp.exit;

import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2023-12-01 01:17
 */
public class DebugSignalHandler implements SignalHandler {
    public static void listenTo(String name) {
        Signal signal = new Signal(name);
        Signal.handle(signal, new DebugSignalHandler());
    }

    public void handle(Signal signal) {
        System.out.println("Signal: " + signal);
        if (signal.toString().trim().equals("SIGTERM")) {
            System.out.println("SIGTERM raised, terminating...");
            System.exit(1);
        }
    }
}
