package com.github.kuangcp.jigsaw;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import lombok.extern.slf4j.Slf4j;

/**
 * created by https://gitee.com/gin9
 * 每个部分的图片类
 *
 * @author kuangcp on 18-9-16-上午11:14
 */
@Slf4j
class Part {

  static int MAX = 3; //拼图的行列规格
  int x;
  int y;
  int position;
  JLabel l;
  Image im;

  Part(int x, int y, int position) {
    this.x = x;
    this.y = y;
    this.position = y + MAX * x;

    try {
      InputStream inputStream = getClass().getClassLoader()
          .getResourceAsStream("jigsaw/" + position + ".jpg");
      im = ImageIO.read(inputStream);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  }
}
