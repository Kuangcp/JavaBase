package com.github.kuangcp.serialize.json.speed;

import com.github.kuangcp.serialize.Person;
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
      GetRunTime getRunTime = new GetRunTime().startCount();

      tool.write(personList);

      getRunTime.endCount(tool.getName());
    }
  }
}