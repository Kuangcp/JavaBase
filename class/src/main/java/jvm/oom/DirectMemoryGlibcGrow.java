package jvm.oom;

import jvm.MallocBench;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * https://github.com/Kuangcp/Note/blob/master/Linux/Base/LinuxBase.md#glibc-ptmalloc2
 *
 * @author Kuangcp
 * 2025-05-15 21:59
 * @see MallocBench
 */
@Slf4j
public class DirectMemoryGlibcGrow {
    //TODO 模拟出Glibc thread arena 占用大的情况
    //TODO 替换 jemalloc 后查看是否有问题


    // https://medium.com/@daniyal.hass/how-glibc-memory-handling-affects-java-applications-the-hidden-cost-of-fragmentation-8e666ee6e000
    public static void fragment() throws InterruptedException {
        List<ByteBuffer> buffers = new ArrayList<>();
        // Allocate large buffers and release them to see memory impact
        for (int i = 0; i < 50; i++) {
            int delta = ThreadLocalRandom.current().nextInt(100) + 33;
            System.out.print(delta + ", ");
            buffers.add(ByteBuffer.allocateDirect(delta * 1024 * 1024)); // 100 MB each

            buffers.remove(0);
//            if (i % 2 == 0) buffers.remove(0); // Simulate variable memory usage
        }

        System.out.println("Memory allocations complete. Check OS memory usage.");
    }

    public static void loopFragment() throws InterruptedException {
        TimeUnit.SECONDS.sleep(10);
        ExecutorService xo = Executors.newFixedThreadPool(3);
        Runnable run = () -> {
            try {
                for (int i = 0; i < 1000; i++) {
                    TimeUnit.MILLISECONDS.sleep(2000 + ThreadLocalRandom.current().nextInt(1000));
                    fragment();
                    System.out.println("loop " + i);
                }
            } catch (Exception e) {
                log.error("", e);
            }
        };
        xo.submit(run);
        xo.submit(run);

    }

}
