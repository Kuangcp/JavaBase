package com.github.kuangcp.serialize.json.speed;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kuangcp.serialize.binary.Person;
import java.util.List;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
public class JacksonTest implements JsonTool {

  @Override
  public void read() {

  }

  @Override
  public void write(int total, List<Person> dataList) {
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
