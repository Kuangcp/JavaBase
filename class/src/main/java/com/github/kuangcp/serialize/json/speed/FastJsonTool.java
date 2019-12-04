package com.github.kuangcp.serialize.json.speed;

import com.alibaba.fastjson.JSON;
import com.github.kuangcp.serialize.Person;
import java.util.List;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
public class FastJsonTool implements JsonTool<Person> {

  @Override
  public Person fromJSON(String json, Class<Person> target) {
    return JSON.parseObject(json, target);
  }

  @Override
  public String toJSON(List<Person> dataList) {
    return JSON.toJSONString(dataList);
  }

  @Override
  public String getName() {
    return "FastJson";
  }
}
