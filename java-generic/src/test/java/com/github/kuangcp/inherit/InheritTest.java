package com.github.kuangcp.inherit;

import com.github.kuangcp.common.Human;
import com.github.kuangcp.common.Student;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * @author kuangcp on 3/6/19-3:16 PM
 */
public class InheritTest {

  @Test
  public void testAdd() {
    Container<Human> humanContainer = new Container<>();
    humanContainer.add(new Human());
    humanContainer.add(new Student());

    Container<Student> studentContainer = new Container<>();
//  x  studentContainer.add(new Human());
    studentContainer.add(new Student());

//  x  Container<Student> temp = humanContainer;
//  x  Container<Human> temp = studentContainer;
  }

  @Test
  public void testInit() {
    Container<Student> a = Container.init(new Student());
//  x  Container<Human> temp = a;
    System.out.println(a.get(0));

    // TODO why?
    Container<Human> b = Container.init(new Student());
    Container<Human> temp = b;
    System.out.println(b.get(0));
    System.out.println(temp.get(0));
  }

  @Test
  public void testArray(){
    // asList 方法签名上的 T 在这里是不唯一的, 可以为String 也可以为Object 都能说的通, 所以看起来这个方法返回值就有很多种
    // 但是当代码已经显式确认 T 的类型时, 后面就是严格按照 泛型的语法 约束了
    // public static <T> List<T> asList(T... a) {

    List<Object> objectList = Arrays.asList("1", "2");

    List<String> strings = Arrays.asList("1", "2");

//  x  List<Object> objects = strings;

    // 可以放任意值 返回值为原始类型, 但是没有警告??
    List data = Arrays.asList(1, "2", 'd', 1.0, new Human());

  }
}