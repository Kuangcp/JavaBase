package thread.waitnotify;

/**
 * @author kuangcp
 */

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * https://www.zhihu.com/question/439926072
 * https://blog.csdn.net/qq_35181209/article/details/77362297
 */
@Slf4j
public class SpuriousWakeup {

    private final List<String> list;

    public SpuriousWakeup() {
        list = Collections.synchronizedList(new LinkedList<>());
    }

    public String removeItemWithWhile() {
        try {
            log.info("in removeItem() - entering");

            synchronized (list) {
                // 当不满足业务判断，等待满足条件，使用if只会判断一次， 应该使用while确保 wait() 方法后的代码满足 业务判断
                while (list.isEmpty()) {
                    log.info("in removeItem() - about to wait()");
                    list.wait();
                    log.info("in removeItem() - done with wait() {}", list.isEmpty());
                }

                //删除元素
                String item = list.remove(0);

                log.info("in removeItem() - leaving");
                return item;
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public String removeItem() {
        try {
            log.info("in removeItem() - entering");

            synchronized (list) {
                // 当不满足业务判断，等待满足条件，使用if只会判断一次， 应该使用while确保 wait() 方法后的代码满足 业务判断
                if (list.isEmpty()) {
                    log.info("in removeItem() - about to wait()");
                    list.wait();
                    log.info("in removeItem() - done with wait()");
                }

                //删除元素
                String item = list.remove(0);

                log.info("in removeItem() - leaving");
                return item;
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public void addItem(String item) {
        log.info("in addItem() - entering");
        synchronized (list) {
            //添加元素
            list.add(item);
            log.info("in addItem() - just added: '" + item + "'");

            //添加后，通知所有线程
            list.notifyAll();
            log.info("in addItem() - just notified");
        }
        log.info("in addItem() - leaving");
    }
}