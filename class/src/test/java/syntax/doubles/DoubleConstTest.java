package syntax.doubles;

import com.github.kuangcp.util.ShowBinary;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 3/14/19-2:20 PM
 * TODO 浮点数 实现原理  Double 实现原理
 */
@Slf4j
public class DoubleConstTest {

  @Test
  public void testExtremum(){
    log.info("{} {}", ShowBinary.toBinary(Double.MIN_VALUE), ShowBinary.toBinary(Double.MIN_NORMAL));
  }
}
