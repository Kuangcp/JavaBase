package base.map;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
public class LifeTimeTest {


  // Map 键相同, 后者覆盖前者, idea 检测到了会直接警告
  @Test
  public void testReplace(){
    Map<String, String> cache = new HashMap<>();

    cache.put("d", "fdjjjjjjj");
    cache.put("d", "111");
    assert cache.size() == 1;
    assert cache.get("d").equals("111");


    Map<Integer, Map<String, String>> cacheMap = new HashMap<>();
    Map<String, String> value = new HashMap<>();
    value.put("1", "1");
    cacheMap.put(1, value);
    Map<String, String> value2 = new HashMap<>();
    value2.put("1", "2");
    cacheMap.put(1, value2);
    assert cacheMap.get(1).get("1").equals("2");

  }
}
