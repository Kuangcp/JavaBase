package thread.waitnotify;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author https://github.com/kuangcp on 2020-05-09 11:01
 */
@Slf4j
public class CallBackHolderTest {

    private final Set<CallBackHolder> set = new HashSet<>();

    /**
     * 随机调用和等待回调结果
     */
    @Test
    public void testLoop() throws InterruptedException {
        List<String> locks = IntStream.range(1, 6).mapToObj(String::valueOf)
                .collect(Collectors.toList());
        for (int i = 0; i < 500; i++) {
            CallBackHolder callBackHolder = new CallBackHolder(locks.get(i % 5));
            set.add(callBackHolder);
        }

        for (CallBackHolder ele : set) {
            new Thread(() -> {
                try {
                    Optional<String> result = ele.call();
                    log.info("result={}", result);
                } catch (InterruptedException e) {
                    log.error("", e);
                }
            }).start();
        }

        for (CallBackHolder ele : set) {
            new Thread(() -> {
                try {
                    Thread.sleep(4000);
                    ele.back(System.currentTimeMillis() + "");
                } catch (InterruptedException e) {
                    log.error("", e);
                }
            }).start();
        }

        Thread.sleep(300000);
    }

    /**
     * 多线程处理 调用和回调
     */
    @Test
    public void testMultiLoop() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(3);
        List<String> locks = IntStream.range(1, 6).mapToObj(String::valueOf)
                .collect(Collectors.toList());
        for (int i = 0; i < 500; i++) {
            CallBackHolder callBackHolder = new CallBackHolder(locks.get(i % 5));
            set.add(callBackHolder);
        }

        for (CallBackHolder ele : set) {
            pool.submit(() -> {
                try {
                    Optional<String> result = ele.call();
                    log.info(": result={}", result);
                } catch (InterruptedException e) {
                    log.error("", e);
                }
            });
        }

        for (CallBackHolder ele : set) {
            pool.submit(() -> {
                try {
                    Thread.sleep(500);
                    ele.back(System.currentTimeMillis() + "");
                } catch (InterruptedException e) {
                    log.error("", e);
                }
            });
        }

        Thread.sleep(300000);
    }


}
