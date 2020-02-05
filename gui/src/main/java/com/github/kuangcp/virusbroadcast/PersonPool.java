package com.github.kuangcp.virusbroadcast;

import com.github.kuangcp.virusbroadcast.domain.City;
import com.github.kuangcp.virusbroadcast.domain.Person;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 */
public class PersonPool {

  private static PersonPool personPool = new PersonPool();

  public static PersonPool getInstance() {
    return personPool;
  }

  public List<Person> personList = new ArrayList<>();

  public List<Person> getPersonList() {
    return personList;
  }

  private PersonPool() {
    City city = new City(400, 400);
    for (int i = 0; i < 500; i++) {
      int x = (int) (100 * ThreadLocalRandom.current().nextGaussian() + city.getCenterX());
      int y = (int) (100 * ThreadLocalRandom.current().nextGaussian() + city.getCenterY());
      if (x > 700) {
        x = 700;
      }
      Person person = new Person(city, x, y);
      personList.add(person);
    }
  }
}
