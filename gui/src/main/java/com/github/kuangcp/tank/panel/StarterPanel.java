package com.github.kuangcp.tank.panel;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * 绘制开始游戏的界面
 * <p>
 * 如果要实现闪烁效果 就实现接口，定义一个变量，run里面++，
 * 设置变量满足什么条件再画出图片
 */
@Slf4j
public class StarterPanel extends JPanel {

    public Image First;

    public StarterPanel() {
        try {
            First = ImageIO.read(getClass().getResource("/tank/img/Tank.jpg"));
        } catch (IOException e) {
            log.error("", e);
        }
    }

    public void paint(Graphics g) {
        g.drawImage(First, 0, 0, 760, 560, this);

        //直接设置字符串，不用图片
        Font my = new Font("微软雅黑", Font.BOLD, 15);
        g.setFont(my);

//        String s = "永无终止";
//        g.setColor(Color.lightGray);
//        g.drawString(s, 50, 500);
    }
}
