package jvm.load;

import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2020-09-12 19:48
 */
@Slf4j
public class ShowClassLoaderTest {

  /**
   * 这里分别对应三种不同类型的类加载器：AppClassLoader、ExtClassLoader 和 BootstrapClassLoader（显示为 null）。
   */
  @Test
  public void test() throws Exception {
    log.info("Classloader of this class:{}", ShowClassLoaderTest.class.getClassLoader());

    log.info("Classloader of Logging:{}",
        com.sun.java.accessibility.util.EventID.class.getClassLoader());

    log.info("Classloader of ArrayList:{}", ArrayList.class.getClassLoader());
  }
}
