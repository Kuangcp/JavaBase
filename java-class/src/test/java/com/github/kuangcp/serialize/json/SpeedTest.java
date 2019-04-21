package com.github.kuangcp.serialize.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.kuangcp.serialize.Person;
import com.github.kuangcp.serialize.json.speed.FastJsonTool;
import com.github.kuangcp.serialize.json.speed.GsonTool;
import com.github.kuangcp.serialize.json.speed.JacksonTool;
import com.github.kuangcp.serialize.json.speed.JsonTool;
import com.github.kuangcp.time.GetRunTime;
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
public class SpeedTest {

  private static final int DATA_SIZE = 3000;

  private List<JsonTool<Person>> list = Arrays
      .asList(new GsonTool(), new JacksonTool(), new FastJsonTool());

  private List<Person> personList = IntStream.range(1, DATA_SIZE)
      .mapToObj(i -> new Person("name" + i)).collect(Collectors.toList());

  @Test
  public void compareRead() {

  }

  @Test
  public void compareWrite() throws JsonProcessingException {
    GetRunTime getRunTime = new GetRunTime();
    for (JsonTool<Person> tool : list) {
      getRunTime.startCount();
      tool.toJSON(personList);
      getRunTime.endCountOneLine(tool.getName());
    }
  }
}