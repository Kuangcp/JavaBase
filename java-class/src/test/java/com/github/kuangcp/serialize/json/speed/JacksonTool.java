package com.github.kuangcp.serialize.json.speed;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kuangcp.serialize.Person;
import java.util.List;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
public class JacksonTool implements JsonTool<Person> {

  @Override
  public void read() {

  }

  @Override
  public void write(List<Person> dataList) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      mapper.writeValueAsString(dataList);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String getName() {
    return "Jackson";
  }
}
