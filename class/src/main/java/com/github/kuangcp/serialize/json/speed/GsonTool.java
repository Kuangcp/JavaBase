package com.github.kuangcp.serialize.json.speed;

import com.github.kuangcp.serialize.Person;
import com.google.gson.Gson;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
@Slf4j
public class GsonTool implements JsonTool<Person> {

  private Gson gson = new Gson();

  @Override
  public Person fromJSON(String json, Class<Person> target) {
    return gson.fromJson(json, target);
  }

  @Override
  public String toJSON(List<Person> dataList) {
    return gson.toJson(dataList);
  }

  @Override
  public String getName() {
    return "GSON";
  }
}
