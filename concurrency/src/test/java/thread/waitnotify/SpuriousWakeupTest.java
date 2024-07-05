package thread.waitnotify;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.function.Supplier;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2024-06-02 17:22
 */
@Slf4j
public class SpuriousWakeupTest {

    private void testLogic(Supplier<String> rmFunc, Runnable addFunc) throws InterruptedException {
        Runnable remove = () -> {
            String item = rmFunc.get();
            log.info("in run() - returned: '" + item + "'");
        };

        //启动第一个删除元素的线程
        Thread threadA1 = new Thread(remove, "threadA1");
        threadA1.start();
        Thread.sleep(500);

        //启动第二个删除元素的线程
        Thread threadA2 = new Thread(remove, "threadA2");
        threadA2.start();
        Thread.sleep(500);

        System.out.println();

        //启动增加元素的线程
        Thread threadB = new Thread(addFunc, "threadB");
        threadB.start();
        Thread.sleep(4000);
        log.info("complete sleep");
    }

    @Test
    public void test() throws InterruptedException {
        final SpuriousWakeup en = new SpuriousWakeup();
        testLogic(en::removeItem, () -> en.addItem("Hello!"));
    }

    @Test
    public void testWithWhile() throws InterruptedException {
        final SpuriousWakeup en = new SpuriousWakeup();
        testLogic(en::removeItemWithWhile, () -> en.addItem("Hello!"));
    }
}
