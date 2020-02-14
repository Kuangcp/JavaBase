package com.github.kuangcp.virusbroadcast.domain;

import static com.github.kuangcp.virusbroadcast.constant.Constants.CITY_PERSON_SCALE;

import com.github.kuangcp.virusbroadcast.constant.Constants;
import com.github.kuangcp.virusbroadcast.constant.PersonState;
import com.github.kuangcp.virusbroadcast.constant.PersonStateEnum;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class City implements Runnable {

  private int centerX = 400;
  private int centerY = 400;
  private int personCount = CITY_PERSON_SCALE;
  private volatile boolean init = false;

  private static City city = new City();
  public List<Person> personList = new CopyOnWriteArrayList<>();
  private static Map<Integer, AtomicInteger> counts;

  static {
    counts = Stream.of(PersonStateEnum.values())
        .collect(Collectors.toMap(PersonStateEnum::getValue, v -> new AtomicInteger(),
            (front, current) -> current));
    counts.get(PersonState.NORMAL).set(CITY_PERSON_SCALE);
  }

  private City() {
    for (int i = 0; i < this.personCount; i++) {
      int x = (int) (100 * ThreadLocalRandom.current().nextGaussian() + centerX);
      int y = (int) (100 * ThreadLocalRandom.current().nextGaussian() + centerY);
      if (x > 700) {
        x = 700;
      }
      Person person = new Person(x, y);
      personList.add(person);
    }
  }

  public void initInfected() {
    // 初始感染
    for (int i = 0; i < Constants.ORIGINAL_COUNT; i++) {
      int index = new Random().nextInt(personList.size() - 1);
      Person person = personList.get(index);

      while (person.isInfected()) {
        index = new Random().nextInt(personList.size() - 1);
        person = personList.get(index);
      }
      person.beInfected();
    }
  }


  public static City getInstance() {
    return city;
  }

  public List<Person> getPersonList() {
    return personList;
  }

  public void dead(Person person) {
    personList.remove(person);
  }

  public static void trans(int from, int to) {
    AtomicInteger fromCount = counts.get(from);
    AtomicInteger toCount = counts.get(to);
    toCount.incrementAndGet();
    fromCount.decrementAndGet();
  }

  @Override
  public void run() {
    while (true) {
      if (!init) {
        this.initInfected();
        this.init = true;
      }
      try {
        Thread.sleep(1000);
        showCityInfo();
      } catch (Exception e) {
        log.error("", e);
      }
    }
  }

  public void showCityInfo() {
    StringBuilder temp = new StringBuilder();
    int sum = 0;
    for (Entry<Integer, AtomicInteger> entry : counts.entrySet()) {
      Optional<PersonStateEnum> stateOpt = PersonStateEnum.getByValue(entry.getKey());
      if (stateOpt.isPresent()) {
        int value = entry.getValue().get();
        temp.append(String.format("%6s: %5d ▌", stateOpt.get().getDesc(), value));
        sum += value;
      }
    }
    log.info("{} sum:{}", temp.toString(), sum);
  }
}
