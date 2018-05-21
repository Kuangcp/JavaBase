package com.github.kuangcp.read;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 * @date 18-5-21  下午2:18
 */
public class ClassScannerTest {

    @Test
    public void testGetPackageAllClasses() throws Exception {
        // 递归找到当前类所在包的所有Logic后缀且继承了抽象逻辑类, 然后就能生成代码了
        List<String> list = new ArrayList<>();
        list.add("*Base");
        ClassScanner scaner = new ClassScanner(true, true, list);
        System.out.println(ClassScannerTest.class.getPackage().getName());
        Set<Class<?>> clazzSet = scaner.getPackageAllClasses(ClassScannerTest.class.getPackage().getName(), true);
        System.out.println(clazzSet.size());
        Iterator<Class<?>> iterator = clazzSet.iterator();
        while (iterator.hasNext()) {
            Class<?> clazz = iterator.next();
            if (clazz != BaseFather.class && BaseFather.class.isAssignableFrom(clazz)) { // 继承自 BaseFather
                String Name = clazz.getSimpleName();
                String name = lowerCaseFirstLetter(Name);
                System.out.println(String.format("    public final static %s %s = new %s();", Name, name, Name));
            }

        }
    }

    private static String lowerCaseFirstLetter(String name) {
        return name.length() == 1 ? name.toLowerCase() : name.substring(0, 1).toLowerCase() + name.substring(1);
    }
}