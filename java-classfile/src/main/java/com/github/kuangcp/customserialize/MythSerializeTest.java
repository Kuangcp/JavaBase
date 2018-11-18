package com.github.kuangcp.customserialize;


/**
 * Created by https://github.com/kuangcp on 17-10-24  下午2:47
 * 测试自定义序列化
 *
 * @author kuangcp
 */
public class MythSerializeTest {

  public static void main(String[] s) {
    testOut();
    testIn();

  }

  public static void testOut() {
    MythSerialize<Myth> mythSerialize = new MythSerialize<>();
    Myth domain = new Myth();
    domain.setName("myth");
    domain.setPhone("121212121");
//        domain.setTest(90909090L);
    mythSerialize.out(domain, "/home/kcp/test/person.txt");
  }

  public static void testIn() {
    MythSerialize<Myth> mythSerialize = new MythSerialize<>();
    Myth domain = (Myth) mythSerialize.in(Myth.class, "/home/kcp/test/person.txt");
    System.out.println(domain.toString());


  }
}
