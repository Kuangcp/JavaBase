package com.github.kuangcp.stream.bug;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2024-07-10 00:06
 */
public class StreamGenericTest {

    @Test
    public void testList() throws Exception {
        final ArrayList list = buildSpecialList();
        final Object result = list.stream()
                .filter(v -> Objects.equals(((HashMap) v).get("name"), "target"))
                .map(v -> ((HashMap) v).get("id"))
                .findAny()
                // 此处的类型转换没有起到作用，只推断为Object类型
                .map(v -> (Integer) v)
                // 即使是硬编码转换类型 同样会只推断为Object类型
                .map(v -> Integer.parseInt(v.toString()))
                // 甚至换成任意对象值，都不符合Optional.map方法的签名(泛型推断)，result 永远推断为Object
//                .map(v -> new HashMap<>())
                .orElse(null);

        // 只能在lambda表达式结束后做强转
        Integer resultId = (Integer) result;
        System.out.println(resultId);

        // 当发起Stream的集合在显式定义泛型后，Optional和Stream的 map方法的泛型就全部正常了。
        final ArrayList<Object> list2 = buildSpecialList();
        final Integer resultId2 = list2.stream()
                .filter(v -> Objects.equals(((HashMap) v).get("name"), "target"))
                .map(v -> ((HashMap) v).get("id"))
                .findAny()
                // 此处的类型转换没有起到作用，修改值也是不起作用，永远返回Object类型
                .map(v -> (Integer) v)
                .orElse(null);
        System.out.println(resultId2);
    }

    private static ArrayList buildSpecialList() {
        final ArrayList list = new ArrayList();

        final HashMap item = new HashMap();
        item.put("name", "target");
        item.put("id", 12);
        list.add(item);


        final HashMap item2 = new HashMap();
        item2.put("name", "other");
        item2.put("id", 1);
        list.add(item2);
        return list;
    }

}
