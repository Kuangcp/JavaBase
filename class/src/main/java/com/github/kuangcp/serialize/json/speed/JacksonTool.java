package com.github.kuangcp.serialize.json.speed;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kuangcp.serialize.Person;
import java.io.IOException;
import java.util.List;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
public class JacksonTool implements JsonTool<Person> {

  public static final ObjectMapper mapper = new ObjectMapper();

  @Override
  public Person fromJSON(String json, Class<Person> target) throws IOException {
    return mapper.readValue(json, target);
  }

  @Override
  public String toJSON(List<Person> dataList) throws JsonProcessingException {
    return mapper.writeValueAsString(dataList);
  }

  @Override
  public String getName() {
    return "Jackson";
  }
}
