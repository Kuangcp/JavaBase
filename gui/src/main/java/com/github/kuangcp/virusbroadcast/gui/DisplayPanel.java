package com.github.kuangcp.virusbroadcast.gui;

import com.github.kuangcp.virusbroadcast.Hospital;
import com.github.kuangcp.virusbroadcast.domain.City;
import com.github.kuangcp.virusbroadcast.constant.PersonState;
import com.github.kuangcp.virusbroadcast.domain.Person;
import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
public class DisplayPanel extends JPanel implements Runnable {

  /**
   * 时间线 day
   */
  public static int worldTime = 0;

  private volatile boolean runnable = true;
  private volatile int infected;


  public DisplayPanel() {
    this.setBackground(new Color(0x444444));
  }

  private static Map<Integer, Color> colorMap = new HashMap<>();

  static {
    colorMap.put(PersonState.NORMAL, Color.white);
    colorMap.put(PersonState.SHADOW, Color.yellow);
    colorMap.put(PersonState.CONFIRMED, Color.red);
    colorMap.put(PersonState.FREEZE, Color.red);
  }

  @Override
  public void paint(Graphics graphics) {
    super.paint(graphics);

    //draw border
    graphics.setColor(Color.green);
    Hospital hospital = Hospital.INSTANCE;
    graphics.drawRect(hospital.getX(), hospital.getY(), hospital.getWidth(), hospital.getHeight());

    List<Person> personList = City.getInstance().getPersonList();

    if (personList.isEmpty()) {
      this.stop(false);
      return;
    }

    int sum = 0;
    for (Person person : personList) {
      if (person.isInfected()) {
        sum++;
      }
      Color color = colorMap.get(person.getState());
      graphics.setColor(color);

      person.update();
      graphics.fillOval(person.getX(), person.getY(), 3, 3);
    }
    this.infected = sum;
    if (sum == 0) {
      this.stop(true);
    }
  }

  @Override
  public void run() {
    while (runnable) {
      this.repaint();

      try {
        Thread.sleep(30);
        worldTime++;
      } catch (InterruptedException e) {
        log.error("", e);
      }
    }
  }

  public synchronized void stop(boolean survival) {
    this.runnable = false;
    if (survival) {
      log.warn("End of epidemic situation!");
    } else {
      log.warn("No one survived!");
    }
  }
}
