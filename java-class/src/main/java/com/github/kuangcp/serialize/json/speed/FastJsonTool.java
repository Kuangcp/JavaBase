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
  public void fromJSON(String json, Class<Person> target) {
    JSON.parseObject(json, target);
  }

  @Override
  public void toJSON(List<Person> dataList) {
    JSON.toJSON(dataList);
  }

  @Override
  public String getName() {
    return "FastJson";
  }
}
