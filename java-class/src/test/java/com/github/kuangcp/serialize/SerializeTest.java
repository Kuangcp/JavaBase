package com.github.kuangcp.serialize;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp on 17-10-24  下午2:27
 * 使用jdk自带的Serializable接口序列化对象 然后反序列化对象
 *
 * @author kuangcp
 */
@Slf4j
public class SerializeTest {


  @Test
  public void testSerializeWithByte() throws IOException, ClassNotFoundException {
    Person person = new Person("name");

    ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
    ObjectOutputStream output = new ObjectOutputStream(byteOutput);
    output.writeObject(person);

    log.info("content={}", byteOutput.toString());

    ByteArrayInputStream byteInput = new ByteArrayInputStream(byteOutput.toByteArray());

    ObjectInputStream input = new ObjectInputStream(byteInput);
    Person result = (Person) input.readObject();
    assertThat(result.getName(), equalTo("name"));
  }

  @Test
  public void testSerializeWithFile() {
    try {
      out();
      in();
    } catch (IOException | ClassNotFoundException e) {
      log.error(e.getMessage(), e);
      Assert.fail();
    }
  }

  private void out() throws IOException {
    Person person = new Person("myth");

    FileOutputStream fileOutputStream = new FileOutputStream("/home/kcp/test/person.md");
    ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
    out.writeObject(person);
    out.close();
    fileOutputStream.close();
    log.debug("序列化完成, person={}", person);
  }

  private void in() throws IOException, ClassNotFoundException {
    FileInputStream fileInputStream = new FileInputStream("/home/kcp/test/object.md");
    ObjectInputStream in = new ObjectInputStream(fileInputStream);
    Person object = (Person) in.readObject();
    in.close();
    fileInputStream.close();
    System.out.println(object.toString());
    assertThat(object.getName(), equalTo("myth"));
  }
}
