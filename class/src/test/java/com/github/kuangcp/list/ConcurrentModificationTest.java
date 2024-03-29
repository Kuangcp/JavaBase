package com.github.kuangcp.list;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * ConcurrentModificationException 的产生是因为 failFast 策略
 * List 发生修改后就会修改 List 的 modCount
 * <p>
 * 当 List 通过迭代器进行迭代时, 每次迭代会将迭代器中的 expectedModCount 和 List 的 modCount 进行比较 如果不一致就抛出异常 快速失败
 * <p>
 * 所以通过下标进行 remove 不会有异常
 *
 * @author kuangcp on 19-1-16-上午9:07
 */
@Slf4j
public class ConcurrentModificationTest {

    // 经测试 ArrayList LinkedList Vector 行为均一致
    private final List<String> list = new ArrayList<>();

    /**
     * 错误
     * <p>
     * 虽然不会抛出异常, 但是有bug, 因为list的长度随着remove在变小,
     * 但是 遍历用的index没有随之变化, 就会出现元素被跳过没有被索引到的情况
     */
    @Test
    public void testNormal() {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals("del")) {
                list.remove(i);
            }
        }

        assertThat(list.size(), equalTo(6));
    }

    /**
     * 错误
     * <p>
     * 反编译后的代码, forEach 实际上是语法糖, next 方法必然抛出异常
     *
     * <pre>
     * Iterator var1 = this.list.iterator();
     * while(var1.hasNext()) {
     *   String x = (String)var1.next();
     *   if (x.equals("del")) {
     *     this.list.remove(x);
     *   }
     * }
     * </pre>
     */
    @Test(expected = ConcurrentModificationException.class)
    public void testForEach() {
        for (String x : list) {
            if (x.equals("del")) {
                // 关键在于这里, 这个remove是使用迭代器遍历list, 找到目标后再复制原数组将对应的对象移除
                // 其中 next() 方法调用了 checkForComodification 方法
                // 由于 remove的调用增加了 modCount 所以在移除了一次后就抛出异常了
                list.remove(x);
                log.info("remove one");
                // 如果这里加上 break语句 就没有异常, 这是因为修改了 modCount 但是不会执行到 next 方法
//        break;
            }
        }
    }

    /**
     * 正确
     * <p>
     * 但是显然的性能低下, 可读性差
     */
    @Test
    public void testNormalCorrect() {
        while (true) {
            int i;
            for (i = 0; i < list.size(); i++) {
                if (list.get(i).equals("del")) {
                    list.remove(i);
                    break;
                }
            }

            // 没有需要移除的元素
            if (i == list.size()) {
                break;
            }
        }

        assertThat(list.size(), equalTo(4));
    }

    /**
     * 正确
     */
    @Test
    public void testIterator() {
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String x = it.next();
            if (x.equals("del")) {
                // remove中有 expectedModCount = modCount; 保证了两个值的一致性 避免了 ConcurrentModificationException
                it.remove();
            }
        }
        assertThat(list.size(), equalTo(4));

        // 以上代码块在Java8中可以简写为  list.removeIf(x -> x.equals("del"));
    }

    @Before
    public void before() {
        list.add("del");
        for (int i = 0; i < 3; i++) {
            list.add(String.valueOf(i));
            list.add("del");
        }

        list.add("del");
        list.add("test");
        list.add("del");
    }

    @After
    public void after() {
        log.info("result: ");
        list.forEach(System.out::println);
    }
}
