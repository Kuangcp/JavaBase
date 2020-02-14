package com.github.kuangcp.virusbroadcast;

import com.github.kuangcp.virusbroadcast.constant.Constants;
import com.github.kuangcp.virusbroadcast.domain.City;
import com.github.kuangcp.virusbroadcast.domain.Person;
import com.github.kuangcp.virusbroadcast.gui.DisplayPanel;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JFrame;

public class VirusBroadcast {

  private static ExecutorService executors = Executors.newFixedThreadPool(2);

  public static void main(String[] args) {

    DisplayPanel panel = new DisplayPanel();
    JFrame frame = new JFrame();
    frame.add(panel);
    frame.setSize(1000, 800);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    executors.submit(panel);
    executors.submit(City.getInstance());

    // 初始感染
    List<Person> people = City.getInstance().getPersonList();
    for (int i = 0; i < Constants.ORIGINAL_COUNT; i++) {
      int index = new Random().nextInt(people.size() - 1);
      Person person = people.get(index);

      while (person.isInfected()) {
        index = new Random().nextInt(people.size() - 1);
        person = people.get(index);
      }
      person.beInfected();
    }
  }
}
