package jvm.oom;

/**
 * set max thread:  echo 1000 > /proc/sys/kernel/threads-max
 * <p>
 * OutOfMemoryError: unable to create new native thread
 *
 * @author https://github.com/kuangcp on 2019-12-25 20:05
 */
public class ThreadOOM {


    private void doLoop() {
        while (true) {
        }
    }

    private void stackLeakByThread() {
        while (true) {
            new Thread(this::doLoop);
        }
    }

    public static void main(String[] args) {
        ThreadOOM oom = new ThreadOOM();
        oom.stackLeakByThread();
    }
}
