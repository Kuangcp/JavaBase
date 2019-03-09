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
 * clone 以及 serialize 创建对象时 不会调用构造器
 */
@Slf4j
public class TargetObjectTest {

  @Test
  public void testInitByNew() {
    new TargetObject();
    new TargetObject("name");
  }

  @Test
  public void testInitByNewInstance() throws IllegalAccessException, InstantiationException {
    TargetObject.class.newInstance();
  }

  @Test
  public void testInitByReflect() throws ReflectiveOperationException {
    Constructor<TargetObject> constructor = TargetObject.class.getConstructor(String.class);

    String name = "use reflect";
    TargetObject domain = constructor.newInstance(name);

    assertThat(domain.getName(), equalTo(name));
  }

  @Test
  public void testInitByClone() throws CloneNotSupportedException {
    TargetObject target = new TargetObject();

    Object clone = target.clone();
    assertThat(target, equalTo(clone));

    assertThat(clone == target, equalTo(false));
  }

  @Test
  public void testInitByDeserialize() throws IOException, ClassNotFoundException {
    TargetObject targetObject = new TargetObject("name");

    ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
    ObjectOutputStream output = new ObjectOutputStream(byteOutput);
    output.writeObject(targetObject);

    ByteArrayInputStream byteInput = new ByteArrayInputStream(byteOutput.toByteArray());

    ObjectInputStream input = new ObjectInputStream(byteInput);
    TargetObject result = (TargetObject) input.readObject();
    assertThat(result.getName(), equalTo("name"));
  }
}
