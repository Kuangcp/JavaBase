package com.github.kuangcp.virusbroadcast;

import com.github.kuangcp.virusbroadcast.domain.City;
import com.github.kuangcp.virusbroadcast.gui.DisplayPanel;

import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class VirusBroadcast {

    private static final ExecutorService executors = new ThreadPoolExecutor(2, 2,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new DefaultThreadFactory("virus"));

    public static void main(String[] args) {
        DisplayPanel panel = new DisplayPanel();
        JFrame frame = new JFrame();
        frame.add(panel);
        frame.setSize(1000, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        executors.submit(panel);
        executors.submit(City.getInstance());
    }
}
