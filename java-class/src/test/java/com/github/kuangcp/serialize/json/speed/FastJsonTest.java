package com.github.kuangcp.serialize.json.speed;

import com.alibaba.fastjson.JSON;
import com.github.kuangcp.serialize.binary.Person;
import java.util.List;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
public class FastJsonTest implements JsonTool {

  @Override
  public void read() {

  }

  @Override
  public void write(int total, List<Person> dataList) {
    JSON.toJSON(dataList);
  }

  @Override
  public String getName() {
    return "FastJson";
  }
}
