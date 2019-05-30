package com.github.kuangcp.jigsaw;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;

/**
 * created by https://gitee.com/gin9
 * 每个部分的图片类
 *
 * @author kuangcp on 18-9-16-上午11:14
 */
@Slf4j
class ImageBlock {


  int x;
  int y;
  int position;
  Image image;

  ImageBlock(int x, int y, int position) {
    this.x = x;
    this.y = y;
    this.position = y + ImageBlockMgr.MAX * x;

    try {
      InputStream inputStream = getClass().getClassLoader()
          .getResourceAsStream("jigsaw/" + position + ".jpg");
      assert inputStream != null;
      image = ImageIO.read(inputStream);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  }
}
