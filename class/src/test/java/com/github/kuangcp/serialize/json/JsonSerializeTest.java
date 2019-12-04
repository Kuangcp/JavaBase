package com.github.kuangcp.serialize.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kuangcp.serialize.Person;
import com.github.kuangcp.serialize.json.speed.FastJsonTool;
import com.github.kuangcp.serialize.json.speed.GsonTool;
import com.github.kuangcp.serialize.json.speed.JacksonTool;
import com.github.kuangcp.serialize.json.speed.JsonTool;
import com.github.kuangcp.time.GetRunTime;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
public class JsonSerializeTest {

  private static final int DATA_SIZE = 30;

  private List<JsonTool<Person>> list = Arrays
      .asList(new GsonTool(), new JacksonTool(), new FastJsonTool());

  private List<Person> personList = IntStream.range(1, DATA_SIZE)
      .mapToObj(i -> new Person("name" + i)).collect(Collectors.toList());

  @Test
  public void compareRead() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    Person one = new Person("one");
    String json = mapper.writeValueAsString(one);

//    Gson gson = new Gson();
//    String json = gson.toJson(one);
    GetRunTime getRunTime = new GetRunTime();
    for (JsonTool<Person> tool : list) {
      getRunTime.startCount();
      Person person = tool.fromJSON(json, Person.class);
//      System.out.println(person);
      getRunTime.endCountOneLine(tool.getName());
    }
  }

  @Test
  public void compareWrite() throws JsonProcessingException {
    GetRunTime getRunTime = new GetRunTime();
    for (JsonTool<Person> tool : list) {
      getRunTime.startCount();
      String json = tool.toJSON(personList);
      System.out.println(json);
      getRunTime.endCountOneLine(tool.getName());
    }
  }
}