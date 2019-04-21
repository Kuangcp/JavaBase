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
  public void fromJSON(String json, Class<Person> target) {
    gson.fromJson(json, target);
  }

  @Override
  public void toJSON(List<Person> dataList) {
    gson.toJson(dataList);
  }

  @Override
  public String getName() {
    return "GSON";
  }
}
