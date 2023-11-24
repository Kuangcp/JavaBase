package com.github.kuangcp.list;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * List Array 互相转换的一些注意点
 */
@Slf4j
public class ListWithArrayTest {

    /**
     * 测试toArray方法，得到的结论就是 无参的 toArray 是复制一份返回一个等大的数组回来
     * <p>
     * toArray(T[]) 是将传入的数组作为容器，将调用方这个数组里的数据复制一份到这个数组里去
     * 若传入的数组空间小了就采取无参的做法新建一个
     * 若空间大了 那么会将结果数组最后一个元素之后的元素设置为 null。
     */
    @Test
    public void testToArray() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        log.debug("{}", list);
        Object[] copy1 = list.toArray();
        log.debug("toArray {}", Arrays.toString(copy1));

        Integer[] target = new Integer[10];
        Object[] copy2 = list.toArray(target);
        log.debug("toArray {}", Arrays.toString(copy2));

        Integer[] target2 = new Integer[5];
        Object[] copy3 = list.toArray(target2);
        log.debug("small than toArray \n{}", Arrays.toString(copy3));

        Integer[] target3 = new Integer[14];
        Object[] copy4 = list.toArray(target3);
        log.debug("bigger than toArray \n{}", Arrays.toString(copy4));
    }

    /**
     * asList 方法的参数是变长参数,也就是 T[]
     */
    @Test
    public void testToList() {
        // 由于基本类型不能用于泛型, 所以不能按设想的那样将data转为list, T 指代了 int[] (原始类型数组是一个对象)
        // 不符合预期
        int[] data = {};
        List<int[]> list = Arrays.asList(data);
        assertThat(list.get(0) instanceof int[], equalTo(true));

        // T 指代了 Integer 符合预期
        Integer[] temp = new Integer[1];
        temp[0] = 3;
        List<Integer> temps = Arrays.asList(temp);
        assertThat(temps.get(0) instanceof Integer, equalTo(true));
    }

}
