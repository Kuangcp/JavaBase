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
public class Panelp extends JPanel implements KeyListener {

  private Move mo = new Move();

  //随机的打乱顺序  应该放在开始按钮里的，按一下就打乱
  private void rr(Move k) {
    for (int i = 0; i < 100; i++) {
      int dir;
      dir = (int) (Math.random() * 5);
      log.debug("dir={}", dir);
      switch (dir) {
        case 1:
          k.moveA();
          repaint();
          break;
        case 2:
          k.moveD();
          repaint();
          break;
        case 3:
          k.moveS();
          repaint();
          break;
        case 4:
          k.moveW();
          repaint();
          break;
        default:
          break;
      }
      Move.count = 1;
    }
  }

  public void paint(Graphics g) {

    for (int i = 0; i < 9; i++) {
      //数组的x是行y是列  画图的时候，x y是像素点的坐标
      g.drawImage(MainFrame.partVector.get(i).im, MainFrame.partVector
              .get(i).y * 200, MainFrame.partVector.get(i).x * 200,
          200, 200,
          null);
    }
  }

  public void keyPressed(KeyEvent e) {
    if (Move.count == 0) {
      rr(mo);
    }
//		System.out.println("开始键盘监听");
    if (e.getKeyCode() == KeyEvent.VK_UP) {
//			System.out.println("w");
      mo.moveW();
      repaint();
    }
    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
//			System.out.println("s");
      mo.moveS();
      repaint();
    }
    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
//			System.out.println("a");
      mo.moveA();
      repaint();
    }
    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
//			System.out.println("d");
      mo.moveD();
      repaint();
    }
    repaint();//继承了JPanel才可以调用repaint函数
  }

  public void keyReleased(KeyEvent e) {
  }


  @Override
  public void keyTyped(KeyEvent e) {

  }
}