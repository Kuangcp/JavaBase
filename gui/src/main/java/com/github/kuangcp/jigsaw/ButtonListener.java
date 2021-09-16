package com.github.kuangcp.jigsaw;

import com.github.kuangcp.tank.constant.ButtonCommand;
import lombok.extern.slf4j.Slf4j;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 18-9-16-上午11:15
 */
@Slf4j
public class ButtonListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        log.debug("开始监听按钮的点击");
        if (e.getActionCommand().equals(ButtonCommand.START)) {
            log.debug("开始游戏");
        }
    }
}

