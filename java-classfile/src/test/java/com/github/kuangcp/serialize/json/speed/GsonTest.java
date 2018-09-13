package com.github.kuangcp.serialize.json.speed;

import com.github.kuangcp.serialize.binary.Person;
import com.google.gson.Gson;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
@Slf4j
public class GsonTest implements JsonTool {

  @Override
  public void read() {

  }

  @Override
  public void write(int total, List<Person> dataList) {
    Gson gson = new Gson();
    gson.toJson(dataList);

  }

  @Override
  public String getName() {
    return "GSON";
  }
}
