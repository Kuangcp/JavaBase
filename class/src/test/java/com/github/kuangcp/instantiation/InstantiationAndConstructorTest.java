package com.github.kuangcp.instantiation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 3/9/19-5:48 PM
 * 实例化和构造器的关系
 *
 * clone 以及 serialize 创建对象时 不会调用构造器
 */
@Slf4j
public class InstantiationAndConstructorTest {

  @Test
  public void testInitByNew() {
    new InstantiationAndConstructor();
    new InstantiationAndConstructor("name");
  }

  @Test
  public void testInitByNewInstance() throws IllegalAccessException, InstantiationException {
    InstantiationAndConstructor.class.newInstance();
  }

  @Test
  public void testInitByReflect() throws ReflectiveOperationException {
    Constructor<InstantiationAndConstructor> constructor = InstantiationAndConstructor.class.getConstructor(String.class);

    String name = "use reflect";
    InstantiationAndConstructor domain = constructor.newInstance(name);

    assertThat(domain.getName(), equalTo(name));
  }

  @Test
  public void testInitByClone() throws CloneNotSupportedException {
    InstantiationAndConstructor target = new InstantiationAndConstructor();

    Object clone = target.clone();
    assertThat(target, equalTo(clone));

    assertThat(clone == target, equalTo(false));
  }

  @Test
  public void testInitByDeserialize() throws IOException, ClassNotFoundException {
    InstantiationAndConstructor targetObject = new InstantiationAndConstructor("name");

    ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
    ObjectOutputStream output = new ObjectOutputStream(byteOutput);
    output.writeObject(targetObject);

    ByteArrayInputStream byteInput = new ByteArrayInputStream(byteOutput.toByteArray());

    ObjectInputStream input = new ObjectInputStream(byteInput);
    InstantiationAndConstructor result = (InstantiationAndConstructor) input.readObject();
    assertThat(result.getName(), equalTo("name"));
  }
}
