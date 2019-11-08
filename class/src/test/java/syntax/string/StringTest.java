package syntax.string;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
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
  public void testConstantPool() {
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

  @Test
  public void testIntern() {
    // 如果在 1.6上运行第一个断言就会通不过
    // intern() 会把首次遇到的字符串复制到常量池中, 并返回常量池中该字符串的引用

    // 在 1.7及以上: 不会复制实例只是在常量池中记录首次出现的实例的引用,
    // 所以intern() 返回的引用和StringBuilder创建的实例是一样的
    String a = new StringBuilder("Ja").append("va").toString();
    assertThat(a.intern(), equalTo(a));

    // 这个断言通过是因为, Java字符串已经不是首次出现了, intern() 返回的是常量池里的引用, 和 StringBuilder实例不一致
    // https://www.zhihu.com/question/51102308 java字符串也是一致的结果
    String b = new StringBuilder("Ja").append("va").toString();
    Assert.assertFalse(b.intern() == b);
  }

  @Test
  public void testReplaceSpecialChar(){
    "".replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");
  }
}
