package com.github.kuangcp.inherit;

import com.github.kuangcp.common.Human;
import com.github.kuangcp.common.Student;
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
}