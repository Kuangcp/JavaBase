package com.github.kuangcp.virusbroadcast;

import com.github.kuangcp.virusbroadcast.domain.City;
import com.github.kuangcp.virusbroadcast.gui.DisplayPanel;
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
  }
}
