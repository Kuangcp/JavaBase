package com.github.kuangcp.jigsaw;

import java.awt.Image;
import java.io.InputStream;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;

/**
 * created by https://gitee.com/gin9
 * 这里是图片移动的功能模块
 *
 * @author kuangcp on 18-9-16-上午10:23
 */
@Slf4j
class Move {

  static int count = 0;
  private Image temp = null;
  private Image im0 = null;
  private int x0, y0, position;

  Move() {
    try {
      InputStream inputStream = getClass().getClassLoader()
          .getResourceAsStream("jigsaw/" + position + ".jpg");

      im0 = ImageIO.read(inputStream);
//      im0 = ImageIO.read(getClass().getResource("/T/8.jpg"));//只是单纯的读取（只有一次）
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 上
   */
  void moveW() {
    for (int i = 0; i < MainFrame.partVector.size(); i++) {
      if (MainFrame.partVector.get(i).im.equals(im0)) {
//	    			log.info("MainFrame.partVector.get(i).x = "+MainFrame.partVector.get(i).x+"MainFrame.partVector.get(i).y = "+MainFrame.partVector.get(i).y);
        x0 = MainFrame.partVector.get(i).x;
        y0 = MainFrame.partVector.get(i).y;
        position = MainFrame.partVector.get(i).position;
      }
    }
    for (int i = 0; i < MainFrame.partVector.size(); i++) {
      if (MainFrame.partVector.get(i).x - 1 == x0 && MainFrame.partVector.get(i).y == y0) {
        temp = MainFrame.partVector.get(i).im;
        MainFrame.partVector.get(i).im = MainFrame.partVector.get(position).im;
        MainFrame.partVector.get(position).im = temp;
        break;
      }
    }
    log.info("W {}", ++count);
  }

  /**
   * 下
   */
  void moveS() {
    for (int i = 0; i < MainFrame.partVector.size(); i++) {
      if (MainFrame.partVector.get(i).im.equals(im0)) {
//	    			log.info("MainFrame.partVector.get(i).x = "+MainFrame.partVector.get(i).x+"MainFrame.partVector.get(i).y = "+MainFrame.partVector.get(i).y);
        x0 = MainFrame.partVector.get(i).x;
        y0 = MainFrame.partVector.get(i).y;
        position = MainFrame.partVector.get(i).position;
      }
    }
//			log.info("x "+x0+" y "+y0+" p "+position);
    for (int i = 0; i < MainFrame.partVector.size(); i++) {
      if (MainFrame.partVector.get(i).x + 1 == x0 && MainFrame.partVector.get(i).y == y0) {
//	    			log.info("MainFrame.partVector.get(i).x = "+MainFrame.partVector.get(i).x+"MainFrame.partVector.get(i).y = "+MainFrame.partVector.get(i).y);
        temp = MainFrame.partVector.get(i).im;
        MainFrame.partVector.get(i).im = MainFrame.partVector.get(position).im;
        MainFrame.partVector.get(position).im = temp;
        break;
      }
    }
    log.info("S {}", ++count);
  }

  /**
   * 左
   */
  void moveA() {
    for (int i = 0; i < MainFrame.partVector.size(); i++) {
      if (MainFrame.partVector.get(i).im.equals(im0)) {
//	    			log.info("MainFrame.partVector.get(i).x = "+MainFrame.partVector.get(i).x+"MainFrame.partVector.get(i).y = "+MainFrame.partVector.get(i).y);
        x0 = MainFrame.partVector.get(i).x;
        y0 = MainFrame.partVector.get(i).y;
        position = MainFrame.partVector.get(i).position;
      }
    }
//			log.info("x "+x0+" y "+y0+" p "+position);
    for (int i = 0; i < MainFrame.partVector.size(); i++) {
      if (MainFrame.partVector.get(i).y - 1 == y0 && MainFrame.partVector.get(i).x == x0) {
//	    			log.info("MainFrame.partVector.get(i).x = "+MainFrame.partVector.get(i).x+"MainFrame.partVector.get(i).y = "+MainFrame.partVector.get(i).y);
        temp = MainFrame.partVector.get(i).im;
        MainFrame.partVector.get(i).im = MainFrame.partVector.get(position).im;
        MainFrame.partVector.get(position).im = temp;
        break;
      }
    }
    log.info("A {}", ++count);
  }

  /**
   * 右
   */
  void moveD() {
    for (int i = 0; i < MainFrame.partVector.size(); i++) {
      if (MainFrame.partVector.get(i).im.equals(im0)) {
//	    			log.info("MainFrame.partVector.get(i).x = "+MainFrame.partVector.get(i).x+"MainFrame.partVector.get(i).y = "+MainFrame.partVector.get(i).y);
        x0 = MainFrame.partVector.get(i).x;
        y0 = MainFrame.partVector.get(i).y;
        position = MainFrame.partVector.get(i).position;
      }
    }
//			log.info("x "+x0+" y "+y0+" p "+position);
    for (int i = 0; i < MainFrame.partVector.size(); i++) {
      if (MainFrame.partVector.get(i).y + 1 == y0 && MainFrame.partVector.get(i).x == x0) {
//	    			log.info("MainFrame.partVector.get(i).x = "+MainFrame.partVector.get(i).x+"MainFrame.partVector.get(i).y = "+MainFrame.partVector.get(i).y);
        temp = MainFrame.partVector.get(i).im;
        MainFrame.partVector.get(i).im = MainFrame.partVector.get(position).im;
        MainFrame.partVector.get(position).im = temp;
        break;
      }
    }
    log.info("D {}", ++count);
  }
}


