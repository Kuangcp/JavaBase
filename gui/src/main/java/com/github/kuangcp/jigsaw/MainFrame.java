package com.github.kuangcp.jigsaw;

import com.github.kuangcp.tank.constant.StageCommand;

import javax.swing.*;
import java.awt.*;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 18-9-16-上午10:22
 */
public class MainFrame extends JFrame {

  public static void main(String[] args) {
    new MainFrame();
    ImageBlockMgr.move.disrupt();
  }

  private MainFrame() throws HeadlessException {
    ButtonListener button = new ButtonListener();
    JButton startBtn = new JButton("开始游戏");
    startBtn.addActionListener(button);//注册监听
    startBtn.setActionCommand(StageCommand.START);//指定特定的命令

    MainPanel btnPanel = new MainPanel();
    btnPanel.add(startBtn, BorderLayout.SOUTH);

    MainPanel mainPanel = new MainPanel();
    mainPanel.setSize(800, 600);
    this.addKeyListener(mainPanel);

    this.add(mainPanel, BorderLayout.CENTER);
//		this.add(btnPanel,BorderLayout.SOUTH);

    //加上按钮的监听后，键盘监听就失效了。？？？
    this.setSize(597, 615);
    this.setLocation(600, 0);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
  }

}
