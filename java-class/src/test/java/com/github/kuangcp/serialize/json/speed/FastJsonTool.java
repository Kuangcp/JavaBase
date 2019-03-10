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
  public void read() {

  }

  @Override
  public void write(List<Person> dataList) {
    JSON.toJSON(dataList);
  }

  @Override
  public String getName() {
    return "FastJson";
  }
}
