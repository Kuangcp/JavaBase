package com.github.kuangcp.generic.constraint;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 不能实例化 参数化类型的数组, 声明是可以的 正确的解决方式应该是 参数化集合
 * 不能实例化 泛型数组
 *
 * @author https://github.com/kuangcp
 * 2019-05-25 21:02
 */
public class GenericArrayTest {

    // 参数化类型
    @Test(expected = ClassCastException.class)
    public void testParametricType() {
        // 不能通过编译
        //    List<String>[] list = new ArrayList<>[3];

        // 经过类型擦除后, 就是 List[] 可以转换为 Object[] 就可以放入任意类型的对象, 但是会抛出 ArrayStoreException

        // 声明为通配类型, 然后类型转换 可以绕过编译检查
        List<String>[] list = (List<String>[]) new ArrayList<?>[3];
        list[0] = new ArrayList();
        List strings = list[0];
        strings.add(12);

        // 因为前面绕过了泛型约束, 这里取值就会将Integer强转为String而抛 ClassCastException 异常
        System.out.println(list[0].get(0));
    }

    // 泛型数组
    @Test
    public void testCreate() {
        String[] ones = create("one", "two");
        ones[0] = "1";

        for (String one : ones) {
            System.out.println(one);
        }
    }

    // https://www.iteye.com/blog/rednaxelafx-379703
    @Test
    public void testCovariantArray() throws Exception {
        Object[] array = new Float[1];
        array[0] = "a string"; // 问题出在这里,编译没问题，但是运行时抛出异常
        array[0].toString();
    }

    // 本质上是得到了 Object[] 强转为 T[]
    private <T> T[] create(T... element) {
        return (T[]) Arrays.asList(element).toArray();
    }

}
