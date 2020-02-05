package com.github.kuangcp.virusbroadcast.gui;

import com.github.kuangcp.virusbroadcast.Hospital;
import com.github.kuangcp.virusbroadcast.domain.Person;
import com.github.kuangcp.virusbroadcast.PersonPool;
import com.github.kuangcp.virusbroadcast.constant.PersonState;
import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
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

  public DisplayPanel() {
    this.setBackground(new Color(0x444444));
  }

  @Override
  public void paint(Graphics arg0) {
    super.paint(arg0);
    //draw border
    arg0.setColor(new Color(0x00ff00));
    arg0.drawRect(Hospital.getInstance().getX(), Hospital.getInstance().getY(),
        Hospital.getInstance().getWidth(), Hospital.getInstance().getHeight());

    List<Person> people = PersonPool.getInstance().getPersonList();
    if (people == null) {
      return;
    }
    int pIndex = 0;
    people.get(pIndex).update();
    for (Person person : people) {

      switch (person.getState()) {
        case PersonState.NORMAL: {
          arg0.setColor(new Color(0xdddddd));

        }
        break;
        case PersonState.SHADOW: {
          arg0.setColor(new Color(0xffee00));

        }
        break;
        case PersonState.CONFIRMED:
        case PersonState.FREEZE: {
          arg0.setColor(new Color(0xff0000));

        }
        break;
      }
      person.update();
      arg0.fillOval(person.getX(), person.getY(), 3, 3);
    }
    pIndex++;
    if (pIndex >= people.size()) {
      pIndex = 0;
    }
  }

  @Override
  public void run() {
    while (true) {
      this.repaint();
      try {
        Thread.sleep(100);
        worldTime++;
      } catch (InterruptedException e) {
        log.error("", e);
      }
    }
  }
}
