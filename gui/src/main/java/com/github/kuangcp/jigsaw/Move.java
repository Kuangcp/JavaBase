package com.github.kuangcp.jigsaw;

import java.awt.Image;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;

/**
 * created by https://gitee.com/gin9
 * 这里是图片移动的功能模块
 *
 * @author kuangcp on 18-9-16-上午10:23
 */
@Slf4j
class Move {

  private static AtomicInteger count = new AtomicInteger();
  private Image image = null;
  private int x, y, position;

  /**
   * 上
   */
  void moveW() {
    refreshValueByImage();
    for (int i = 0; i < ImageBlockMgr.images.size(); i++) {
      if (ImageBlockMgr.images.get(i).x - 1 == x && ImageBlockMgr.images.get(i).y == y) {
        cacheImage(i);
        break;
      }
    }
    log.info("W {}", count.incrementAndGet());
  }

  /**
   * 下
   */
  void moveS() {
    refreshValueByImage();

//			log.info("x "+x+" y "+y+" p "+position);
    for (int i = 0; i < ImageBlockMgr.images.size(); i++) {
      if (ImageBlockMgr.images.get(i).x + 1 == x && ImageBlockMgr.images.get(i).y == y) {
        ImageBlockMgr.show(i);
        cacheImage(i);
        break;
      }
    }
    log.info("S {}", count.incrementAndGet());
  }

  /**
   * 左
   */
  void moveA() {
    refreshValueByImage();
//			log.info("x "+x+" y "+y+" p "+position);
    for (int i = 0; i < ImageBlockMgr.images.size(); i++) {
      if (ImageBlockMgr.images.get(i).y - 1 == y && ImageBlockMgr.images.get(i).x == x) {
        ImageBlockMgr.show(i);
        cacheImage(i);
        break;
      }
    }
    log.info("A {}", count.incrementAndGet());
  }

  /**
   * 右
   */
  void moveD() {
    refreshValueByImage();
//			log.info("x "+x+" y "+y+" p "+position);
    for (int i = 0; i < ImageBlockMgr.images.size(); i++) {
      if (ImageBlockMgr.images.get(i).y + 1 == y && ImageBlockMgr.images.get(i).x == x) {
        ImageBlockMgr.show(i);
        cacheImage(i);
        break;
      }
    }
    log.info("D {}", count.incrementAndGet());
  }

  private void refreshValueByImage() {
    for (int i = 0; i < ImageBlockMgr.images.size(); i++) {
      if (ImageBlockMgr.isSameImage(i, image)) {
        ImageBlockMgr.show(i);
        cacheValue(ImageBlockMgr.getImageBlock(i));
      }
    }
  }

  private void cacheValue(ImageBlock imageBlock) {
    x = imageBlock.x;
    y = imageBlock.y;
    position = imageBlock.position;
  }

  private void cacheImage(int i) {
    ImageBlock target = ImageBlockMgr.images.get(i);
    ImageBlock origin = ImageBlockMgr.images.get(position);

    Image temp = target.image;
    target.image = origin.image;
    origin.image = temp;
  }

  /**
   * 打乱顺序
   */
  void disrupt() {
    for (int i = 0; i < 80; i++) {
      int dir;
      dir = (int) (Math.random() * 5);
      switch (dir) {
        case 1:
          moveA();
          break;
        case 2:
          moveD();
          break;
        case 3:
          moveS();
          break;
        case 4:
          moveW();
          break;
        default:
          break;
      }
    }
  }

}


