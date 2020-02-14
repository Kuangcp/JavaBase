package com.github.kuangcp.virusbroadcast.domain;

import static com.github.kuangcp.virusbroadcast.constant.Constants.SAFE_DISTANCE;

import com.github.kuangcp.virusbroadcast.Hospital;
import com.github.kuangcp.virusbroadcast.constant.Constants;
import com.github.kuangcp.virusbroadcast.constant.PersonState;
import com.github.kuangcp.virusbroadcast.gui.DisplayPanel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Data
@Slf4j
public class Person {

  private Bed bed;
  private int x;
  private int y;
  private MoveTarget moveTarget;
  private int state = PersonState.NORMAL;
  private static Map<Integer, Consumer<Person>> functions = new HashMap<>();

  int sig = 1;
  /**
   * 被感染时间
   */
  int infectedTime = 0;
  /**
   * 确诊时间
   */
  int confirmedTime = 0;

  double targetXU;
  double targetYU;
  double targetSig = 50;

  public Person(int x, int y) {
    this.x = x;
    this.y = y;
    targetXU = 100 * ThreadLocalRandom.current().nextGaussian() + x;
    targetYU = 100 * ThreadLocalRandom.current().nextGaussian() + y;
  }

  public boolean wantMove() {
    double value = sig * ThreadLocalRandom.current().nextGaussian() + Constants.INTENTION;
    return value > 0;
  }

  public boolean isInfected() {
    return state >= PersonState.SHADOW && state != PersonState.DEAD;
  }

  /**
   * 被感染
   */
  public void beInfected() {
    if (this.isInfected()) {
      return;
    }
    City.trans(PersonState.NORMAL, PersonState.SHADOW);
    state = PersonState.SHADOW;
    infectedTime = DisplayPanel.worldTime;
  }

  public void confirmed() {
    City.trans(PersonState.SHADOW, PersonState.CONFIRMED);
    state = PersonState.CONFIRMED;
    confirmedTime = DisplayPanel.worldTime;
  }

  public void willInfected() {
    if (this.isInfected()) {
      return;
    }

    // 是否能被其他人感染
    List<Person> people = City.getInstance().personList;
    for (Person person : people) {
      if (person.getState() == PersonState.NORMAL) {
        continue;
      }

      if (this.mayInfected(person)) {
        this.beInfected();
      }
    }
  }

  /**
   * 是否感染
   */
  public boolean mayInfected(Person person) {
    if (person.getState() == PersonState.NORMAL) {
      return false;
    }
    float random = ThreadLocalRandom.current().nextFloat();
    return random < Constants.BROAD_RATE && distance(person) < SAFE_DISTANCE;
  }

  public double distance(Person person) {
    return Math.sqrt(Math.pow(x - person.getX(), 2) + Math.pow(y - person.getY(), 2));
  }

  private void moveTo(int x, int y) {
    this.x += x;
    this.y += y;
  }

  private boolean freeze(Bed bed) {
    if (Objects.nonNull(this.bed) || Objects.isNull(bed)) {
//      System.out.println("隔离区没有空床位");
      return false;
    }

    City.trans(PersonState.CONFIRMED, PersonState.FREEZE);
    this.bed = bed;
    state = PersonState.FREEZE;
    x = bed.getX();
    y = bed.getY();
    bed.setEmpty(false);
    return true;
  }

  private boolean cure() {
    City.trans(PersonState.FREEZE, PersonState.NORMAL);
    if (Objects.isNull(this.bed)) {
      return false;
    }
    this.bed.setEmpty(true);
    this.bed = null;
    this.state = PersonState.NORMAL;
    return true;
  }

  private boolean dead() {
    City.trans(this.state, PersonState.DEAD);
    if (Objects.nonNull(this.bed)) {
      this.bed.setEmpty(true);
      this.bed = null;
    }
    this.state = PersonState.DEAD;
    return true;
  }

  // 随机移动
  private void action() {
    if (state == PersonState.FREEZE) {
      return;
    }

    if (!wantMove()) {
      return;
    }

    if (moveTarget == null || moveTarget.isArrived()) {
      double targetX = targetSig * ThreadLocalRandom.current().nextGaussian() + targetXU;
      double targetY = targetSig * ThreadLocalRandom.current().nextGaussian() + targetYU;
      moveTarget = new MoveTarget((int) targetX, (int) targetY);
    }

    int dX = moveTarget.getX() - x;
    int dY = moveTarget.getY() - y;
    double length = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));

    if (length < 1) {
      moveTarget.setArrived(true);
      return;
    }
    int udX = (int) (dX / length);
    if (udX == 0 && dX != 0) {
      if (dX > 0) {
        udX = 1;
      } else {
        udX = -1;
      }
    }
    int udY = (int) (dY / length);
    if (udY == 0 && udY != 0) {
      if (dY > 0) {
        udY = 1;
      } else {
        udY = -1;
      }
    }

    if (x > 700) {
      moveTarget = null;
      if (udX > 0) {
        udX = -udX;
      }
    }
    this.moveTo(udX, udY);
  }

  public void update() {
    functions.get(state).accept(this);
  }

  static {
    functions.put(PersonState.NORMAL, p -> {
      p.action();
      p.willInfected();
    });

    functions.put(PersonState.SHADOW, p -> {
      if (DisplayPanel.worldTime - p.infectedTime > Constants.SHADOW_TIME) {
        p.confirmed();
      }
      p.action();
    });

    functions.put(PersonState.CONFIRMED, p -> {
      if (DisplayPanel.worldTime - p.confirmedTime >= Constants.HOSPITAL_RECEIVE_TIME) {
        Bed bed = Hospital.INSTANCE.pickBed();
        p.freeze(bed);
      } else {
        p.action();
      }

      int rate = ThreadLocalRandom.current().nextInt(Constants.BASE_RATE);
      if (rate <= Constants.DEAD_RATE) {
        p.dead();
      }
    });

    functions.put(PersonState.FREEZE, p -> {
      // 看作数轴上两个线段
      int rate = ThreadLocalRandom.current().nextInt(Constants.BASE_RATE);
      if (rate <= Constants.CURE_RATE) {
        p.cure();
      } else if (rate <= Constants.DEAD_RATE + Constants.CURE_RATE) {
        p.dead();
      }
    });

    functions.put(PersonState.DEAD, p -> {
//      log.info("dead: person={}", p);
      City.getInstance().dead(p);
    });
  }
}
