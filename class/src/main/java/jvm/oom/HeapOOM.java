package jvm.oom;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author kuangcp on 4/3/19-10:11 PM
 */
@Slf4j
public class HeapOOM {

    public void createArray() {
        List<byte[]> data = new ArrayList<>();
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                log.error("", e);
            }
            log.info("size={}", data.size());
            data.add(new byte[1024 * 1024]);
        }
    }

    public void createArrayRecovery() {
        try {
            List<byte[]> data = new ArrayList<>();
            while (true) {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    log.error("", e);
                }
                log.info("size={}", data.size());
                data.add(new byte[1024 * 1024]);
            }
            // 如果是 Exception 就无法捕获OOM Error 同样会中断执行
            // 如果能Catch到OOMError，就可以维持线程的继续执行 Throwable Error 等都可以实现
//        } catch (Exception e) {
        } catch (Throwable e) {
            log.error("", e);
        }

        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                log.error("", e);
            }
            log.info("do something");
        }
    }

    /**
     * 重写了hashCode 没有重写 equals
     * 导致了该对象在Map这样的集合中作为Key使用时, 不能按预期的覆盖旧值而是共存
     */
    static class Key {

        Integer id;

        Key(Integer id) {
            this.id = id;
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }

    void createMap() {
        Map<Key, String> map = new HashMap<>();
        testMap(map);
    }

    private void testMap(Map<Key, String> m) {
        while (true) {
            for (int i = 0; i < 10000; i++) {
                // 因此这个判断就失效了
                if (!m.containsKey(new Key(i))) {
                    m.put(new Key(i), "Number:" + i);
                }
            }
            log.info("m.size()=" + m.size());
        }
    }

    /**
     * 因为 WeakMap 中 如果键没有被外部引用, 就有可能被GC, 当内存不够就会频繁GC, 也就不会OOM
     */
    void createWeakMap() {
        testMap(new WeakHashMap<>());
    }

    /**
     * 变量逃逸和不逃逸 的区别
     * <p>
     * 当一个线程发生OOM, 如果能够及时释放内存(堆内存足够大,线程占用CPU不是很高等等), 是不会影响到其他线程的
     */
    public void otherThreadWithOOM() throws Exception {
        // 如果将该集合定义在这里 就会被所有线程共享 被引用数会多, 不容易回收, 就容易引起全面的OOM 导致进程退出
//    List<byte[]> data = new ArrayList<>();

        Thread allocateMemory = new Thread(() -> {
            // 但是定义在这里, 就只会被该线程持有, OOM 也只是该线程, 只有小概率会影响到进程内其他线程
            List<byte[]> data = new ArrayList<>();
            while (true) {
                // 因为当这里无法分配内存 OOM 后就出循环了, 对象也就可以被回收了
                data.add(new byte[1024 * 10]);
                try {
                    TimeUnit.MILLISECONDS.sleep(4);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
//        log.info("allocate memory");
            }
        });

        Thread timer = new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
                log.info("timer");
            }
        });

        allocateMemory.setName("allocateMemory");
        timer.setName("timer");

        allocateMemory.start();
        timer.start();
        allocateMemory.join();
        timer.join();
    }
}
