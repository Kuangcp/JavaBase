package com.github.kuangcp.reflects;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/kuangcp">Github</a>
 * 2023-11-14 19:49
 */
@Slf4j
public class BeanUtil {

    private static final Map<Class<?>, Map<String, Field>> cache = new WeakHashMap<>();

    public static void copyProperties(Object src, Object target) {
        copyProperties(src, target, false);
    }

    /**
     * @param source     源对象
     * @param target     目标对象
     * @param ignoreCase 是否忽略大小写
     */
    public static void copyProperties(Object source, Object target, boolean ignoreCase) {
        if (source == null || target == null) {
            return;
        }
        Map<String, Field> sf = fieldMap(source);

        Map<String, Field> tf = fieldMap(target);

        try {
            for (Map.Entry<String, Field> entry : tf.entrySet()) {
                Field sField = sf.get(entry.getKey());
                Object sval = sField.get(source);
                Field tField = tf.get(entry.getKey());
                tField.set(target, sval);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public static Map<String, Field> fieldMap(Object source) {
        return fieldMap(source.getClass());
    }

    public static Map<String, Field> fieldMap(Class<?> clz) {
        return cache.computeIfAbsent(clz, v -> {
            log.info("init");
            Field[] fields = clz.getDeclaredFields();
            return Arrays.stream(fields).collect(Collectors
                    .toMap(Field::getName, d -> {
                        d.setAccessible(true);
                        return d;
                    }, (front, current) -> current));
        });
    }
}
