package com.github.kuangcp.generic.inherit;

import com.github.kuangcp.common.Human;
import com.github.kuangcp.common.Student;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author kuangcp on 3/6/19-3:16 PM
 */
public class InheritTest {

  // test Arrays.asList() method
  @Test
  public void testArray() {
    // Type T on method signature is not unique, both String and Object satisfy generic rule
    // generic rule is only exist at compile time
    // but when generics are explicitly specified, the T will be identified

    List<Object> objectList = Arrays.asList("1", "2");
    List<String> strings = Arrays.asList("1", "2");
//  x  List<Object> objects = strings;

    // 可以放任意值 返回值为原始类型, 但是没有警告??
    List<Object> data = Arrays.asList(1, "2", 'd', 1.0, new Human());
  }

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
}