package base.define;

import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
public class ManagerTest {



  @Test
  public void testMultiple(){
    Manager.manager.setName("test1");
    System.out.println(Manager.manager.getName());
    System.out.println(Manager.manager.hashCode());
//    Manager.manager.setName("test2");
    System.out.println(Manager.manager.getName());
    System.out.println(Manager.manager.hashCode());

  }
}
