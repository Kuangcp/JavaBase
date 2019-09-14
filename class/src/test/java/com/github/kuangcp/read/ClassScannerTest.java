package com.github.kuangcp.read;

import com.github.kuangcp.io.ResourceTool;
import com.github.kuangcp.time.GetRunTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;


/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
@Slf4j
public class ClassScannerTest {

  @Test
  public void testGetPackageAllClasses() {
    // 递归找到当前类所在包的所有 Base 后缀且继承了抽象逻辑类, 然后生成代码
    List<String> list = new ArrayList<>();
    list.add("*Base");
    ClassScanner scanner = new ClassScanner(true, true, list);

    System.out.println(ClassScannerTest.class.getPackage().getName());
    Set<Class<?>> clazzSet = scanner
        .getPackageAllClasses(ClassScannerTest.class.getPackage().getName(), true);

    log.info("size: {}", clazzSet.size());

    for (Class<?> clazz : clazzSet) {
      if (clazz != BaseFather.class && BaseFather.class.isAssignableFrom(clazz)) { // 继承自 BaseFather
        String rawName = clazz.getSimpleName();
        String name = lowerCaseFirstLetter(rawName);
        System.out.println(String.format
            ("    public final static %s %s = new %s();", rawName, name, rawName));
      }
    }
  }

  @Test
  public void testReadJar(){
    GetRunTime getRunTime = new GetRunTime().startCount();
    ClassScanner scanner = new ClassScanner(true, true, Collections.emptyList());
    String path = ResourceTool.class.getPackage().getName();
    log.info("path={}", path);
    Set<Class<?>> result = scanner.getPackageAllClasses(path, true);
    log.info("result {}", result.size());
    result.forEach(System.out::println);
    getRunTime.endCountOneLine("");
  }

  private static String lowerCaseFirstLetter(String name) {
    return name.length() == 1 ? name.toLowerCase()
        : name.substring(0, 1).toLowerCase() + name.substring(1);
  }
}