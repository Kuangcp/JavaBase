package syntax.string;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
@Slf4j
public class StringTest {

  // Forced type conversion
  @Test
  public void testConvert() {
    List<Object> list = new ArrayList<>();
    list.add(1);
    Object name = list.get(0);
    log.debug("name: {}", name);
    log.debug("is null : {}", name == null);
    log.debug("is String : {}", name instanceof String);
    log.debug("is int : {}", name instanceof Integer);

    list.add(null);
    list.add(null);

    log.debug("list.size {}", list.size());

    if (list.get(0) instanceof String) {
      String nullToString = (String) list.get(0);
      log.debug("string: {}", nullToString);
    } else {
      log.debug("not String : {}", list.get(0));
    }
  }

  @Test
  public void testUUID() {
    String uuid = UUID.randomUUID().toString();
    log.debug("uuid: ={}", uuid);
  }

  @Test
  public void testFinalPool() {
    assert new String("1") != new String("1");
    assert "1" != new String("1");
    assert "11" != new String("1") + new String("1");

    // 常量池
    assert "1" == "1";

    // 左边在常量池 右边在堆, 即使 HashCode 一致
    assert "1" + "1" != new String("1") + new String("1");
    assert ("1" + "1").hashCode() == (new String("1") + new String("1")).hashCode();

    // 以下三种 右边表达式的结果都是等价的, 都在堆创建了一个新的对象
    String a = new String("1") + new String("1");
//    String a = new String("1" + "1");
//    String a = new String("1" ) + "1";

    // https://docs.oracle.com/javase/8/docs/api/java/lang/String.html#intern--
    // When the intern method is invoked,
    // if the pool already contains a string equal to this String object as determined by the equals(Object) method, then the string from the pool is returned.
    // Otherwise, this String object is added to the pool and a reference to this String object is returned.
    String b = a.intern();
    String c = "11";

    assert a != b;
    assert b == c;
    assert a != c;
  }
}
