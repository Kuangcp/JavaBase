package com.github.kuangcp.serialize.json.speed;

import static com.github.kuangcp.time.GetRunTime.GET_RUN_TIME;

import com.github.kuangcp.serialize.binary.Person;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
public class CompareMain {

  private int writeTotal = 200000;

  private List<JsonTool> list = Arrays
      .asList(new GsonTest(), new JacksonTest(), new FastJsonTest());
  private List<Person> personList = new ArrayList<>();

  {
    for (int i = 0; i < writeTotal; i++) {
      Person person = new Person();
      person.setAddress("address" + i);
      person.setName("name" + i);
      person.setPhone("phone" + i);
      personList.add(person);
    }
  }

  @Test
  public void compareRead() {

  }

  @Test
  public void compareWrite() {
    for (JsonTool item : list) {
      GET_RUN_TIME.startCount();
      item.write(writeTotal, personList);
      GET_RUN_TIME.endCount(item.getName());
//      try {
//        Thread.sleep(10);
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
    }

  }
}