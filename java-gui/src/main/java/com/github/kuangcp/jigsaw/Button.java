package com.github.kuangcp.jigsaw;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import lombok.extern.slf4j.Slf4j;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 18-9-16-上午11:15
 */
@Slf4j
public class Button implements ActionListener {

  public void actionPerformed(ActionEvent e) {
    log.debug("开始监听按钮的点击");
    if (e.getActionCommand().equals("开始")) {
      log.debug("开始游戏");
    }
  }
}

