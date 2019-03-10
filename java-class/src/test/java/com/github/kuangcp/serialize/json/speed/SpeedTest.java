package com.github.kuangcp.serialize.json.speed;

import static com.github.kuangcp.time.GetRunTime.GET_RUN_TIME;

import com.github.kuangcp.serialize.Person;
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

  private List<JsonTool<Person>> list = Arrays
      .asList(new GsonTool(), new JacksonTool(), new FastJsonTool());

  private List<Person> personList = IntStream.range(1, 2000)
      .mapToObj(i -> new Person("name" + i)).collect(Collectors.toList());

  @Test
  public void compareRead() {

  }

  @Test
  public void compareWrite() {
    for (JsonTool<Person> tool : list) {
      GET_RUN_TIME.startCount();

      tool.write(personList);

      GET_RUN_TIME.endCount(tool.getName());
    }
  }
}