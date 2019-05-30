package com.github.kuangcp.jigsaw;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import lombok.extern.slf4j.Slf4j;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 18-9-16-上午10:24
 */
@Slf4j
public class MainPanel extends JPanel implements KeyListener {


  @Override
  public void paint(Graphics graphics) {
    for (int i = 0; i < 9; i++) {
      //数组的x是行y是列  画图的时候，x y是像素点的坐标
      graphics.drawImage(ImageBlockMgr.getImage(i),
          ImageBlockMgr.getStartX(i), ImageBlockMgr.getStartY(i),
          ImageBlockMgr.IMAGE_SIZE, ImageBlockMgr.IMAGE_SIZE, null);
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
//    if (Move.count == 0) {
//      randomKey(move);
//    }

    if (e.getKeyCode() == KeyEvent.VK_UP) {
      ImageBlockMgr.move.moveW();
      repaint();
    }
    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
      ImageBlockMgr.move.moveS();
      repaint();
    }
    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
      ImageBlockMgr.move.moveA();
      repaint();
    }
    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
      ImageBlockMgr.move.moveD();
      repaint();
    }

    //继承了JPanel才可以调用repaint函数
    repaint();
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }
}