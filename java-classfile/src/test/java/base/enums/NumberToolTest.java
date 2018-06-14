package base.enums;

import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
public class NumberToolTest {
  @Test
  public void testInit(){
    System.out.println(NumberTool.INSTANCE);
    System.out.println(NumberTool.INSTANCE.setMax(333));
    System.out.println(NumberTool.INSTANCE);
    System.out.println(NumberTool.INSTANCE);
  }

  public static void main(String[]a){
    new Thread(() -> System.out.println(NumberTool.INSTANCE));
    new Thread(() -> System.out.println(NumberTool.INSTANCE));
    new Thread(() -> System.out.println(NumberTool.INSTANCE));
    new Thread(() -> System.out.println(NumberTool.INSTANCE));
  }

}