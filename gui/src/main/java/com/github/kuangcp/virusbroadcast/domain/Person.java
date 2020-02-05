package com.github.kuangcp.virusbroadcast.domain;

import static com.github.kuangcp.virusbroadcast.constant.Constants.SAFE_DISTANCE;

import com.github.kuangcp.virusbroadcast.Hospital;
import com.github.kuangcp.virusbroadcast.PersonPool;
import com.github.kuangcp.virusbroadcast.constant.Constants;
import com.github.kuangcp.virusbroadcast.constant.PersonState;
import com.github.kuangcp.virusbroadcast.gui.DisplayPanel;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Data;

/**
 *
 */
@Data
public class Person {

  private City city;
  private int x;
  private int y;
  private MoveTarget moveTarget;
  private int state = PersonState.NORMAL;

  int sig = 1;
  int infectedTime = 0;
  int confirmedTime = 0;

  double targetXU;
  double targetYU;
  double targetSig = 50;

  public Person(City city, int x, int y) {
    this.city = city;
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
    return state >= PersonState.SHADOW;
  }

  public void beInfected() {
    state = PersonState.SHADOW;
    infectedTime = DisplayPanel.worldTime;
  }

  /**
   * 是否感染
   */
  public boolean willInfected(Person person) {
    if (person.getState() == PersonState.NORMAL) {
      return false;
    }
    float random = ThreadLocalRandom.current().nextFloat();
    return random < Constants.BROAD_RATE && distance(person) < SAFE_DISTANCE;
  }

  public double distance(Person person) {
    return Math.sqrt(Math.pow(x - person.getX(), 2) + Math.pow(y - person.getY(), 2));
  }

  private void freezy() {
    state = PersonState.FREEZE;
  }

  private void moveTo(int x, int y) {
    this.x += x;
    this.y += y;
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
    moveTo(udX, udY);
  }

  public void update() {
    //TODO 找时间改为状态机
    if (state >= PersonState.FREEZE) {
      return;
    }
    if (state == PersonState.CONFIRMED
        && DisplayPanel.worldTime - confirmedTime >= Constants.HOSPITAL_RECEIVE_TIME) {
      Bed bed = Hospital.getInstance().pickBed();
      if (bed == null) {
//        System.out.println("隔离区没有空床位");
      } else {
        state = PersonState.FREEZE;
        x = bed.getX();
        y = bed.getY();
        bed.setEmpty(false);
      }
    }
    if (DisplayPanel.worldTime - infectedTime > Constants.SHADOW_TIME
        && state == PersonState.SHADOW) {
      state = PersonState.CONFIRMED;
      confirmedTime = DisplayPanel.worldTime;
    }

    this.action();

    List<Person> people = PersonPool.getInstance().personList;
    if (state >= PersonState.SHADOW) {
      return;
    }
    for (Person person : people) {
      if (person.getState() == PersonState.NORMAL) {
        continue;
      }

      if (this.willInfected(person)) {
        this.beInfected();
      }
    }
  }
}
