package com.github.kuangcp.virusbroadcast.gui;

import com.github.kuangcp.virusbroadcast.Hospital;
import com.github.kuangcp.virusbroadcast.constant.PersonState;
import com.github.kuangcp.virusbroadcast.domain.City;
import com.github.kuangcp.virusbroadcast.domain.Person;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public DisplayPanel() {
        this.setBackground(new Color(0x444444));
    }

    private static final Map<Integer, Color> colorMap = new HashMap<>();

    static {
        colorMap.put(PersonState.NORMAL, Color.white);
        colorMap.put(PersonState.SHADOW, Color.yellow);
        colorMap.put(PersonState.CONFIRMED, Color.red);
        colorMap.put(PersonState.FREEZE, Color.blue);
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);

        //draw border
        graphics.setColor(Color.blue);
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
        final City.Status status = City.getInstance().buildCityInfo();
        String text = "day:" + worldTime + " " + status.toString();
        graphics.setColor(Color.yellow);
        if (sum == 0) {
            text = "End of epidemic situation!";
            graphics.drawString(text, 10, 10);
            this.stop(true);
        } else {
            graphics.drawString(text, 10, 10);
        }
    }

    @Override
    public void run() {
        while (runnable) {
            this.repaint();

            try {
                Thread.sleep(20);
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
