package com.github.kuangcp.serialize.binary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp on 17-10-24  下午2:27
 * 使用jdk自带的Serializable接口序列化对象 然后反序列化对象
 * 意义在于能够让反射更为准确，装载对象？？
 *
 * @author kuangcp
 */
@Slf4j
public class SerializeTest {

  /**
   * 使用JDK的接口 序列化对象 并输出
   */
  @Test
  public void out() {
    Person person = new Person();
    person.setAddress("12");
    person.setName("myth");
    person.setPhone("123456654");
    try {
      String absolutePath = new File("").getAbsolutePath();
      System.out.println(absolutePath);

      // TODO 相对路径, 绝对路径;

      FileOutputStream fileOutputStream = new FileOutputStream("/home/kcp/test/person.md");
      ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
      out.writeObject(person);
      out.close();
      fileOutputStream.close();
      log.debug("序列化完成, person={}", person);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  }

  /**
   * 反序列化
   */
  @Test
  public void in() {
    try {
      FileInputStream fileInputStream = new FileInputStream("/home/kcp/test/person.md");
      ObjectInputStream in = new ObjectInputStream(fileInputStream);
      Person person = (Person) in.readObject();
      in.close();
      fileInputStream.close();
      System.out.println(person.toString());
    } catch (IOException | ClassNotFoundException e) {
      log.error(e.getMessage(), e);
    }
  }
}
