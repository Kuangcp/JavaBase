package com.github.kuangcp.jigsaw;

import java.awt.Image;
import java.util.Objects;
import java.util.Optional;
import java.util.Vector;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kuangcp on 2019-04-29 11:58 PM
 */
@Slf4j
class ImageBlockMgr {

  static int MAX = 3; //拼图的行列大小
  static int IMAGE_SIZE = 200;// 200 x 200

  static Move move = new Move();
  static Vector<ImageBlock> images = new Vector<>(ImageBlockMgr.MAX * ImageBlockMgr.MAX);

  static {
    for (int i = 0; i < ImageBlockMgr.MAX * ImageBlockMgr.MAX; i++) {
      ImageBlock p = new ImageBlock(i / ImageBlockMgr.MAX, i % ImageBlockMgr.MAX, i);
      images.add(p);
    }
  }

  static void show(int index) {
    Optional<ImageBlock> imageBlockOpt = get(index);
    imageBlockOpt.ifPresent(imageBlock ->
        log.info("image: x = " + imageBlock.x + "y = " + imageBlock.y));
  }

  static boolean isSameImage(int index, Image image) {
    return Objects.equals(getImage(index), image);
  }

  static ImageBlock getImageBlock(int index) {
    Optional<ImageBlock> imageBlock = get(index);
    assert imageBlock.isPresent();
    return imageBlock.get();
  }

  private static Optional<ImageBlock> get(int index) {
    return Optional.ofNullable(images.get(index));
  }

  static Image getImage(int index) {
    ImageBlock imageBlock = images.get(index);
    if (Objects.nonNull(imageBlock)) {
      return imageBlock.image;
    }

    log.error("invalid: index={}", index);
    return null;
  }

  static int getStartX(int index) {
    ImageBlock imageBlock = images.get(index);
    if (Objects.nonNull(imageBlock)) {
      return imageBlock.x * IMAGE_SIZE;
    }

    log.error("invalid: index={}", index);
    return 0;
  }

  static int getStartY(int index) {
    ImageBlock imageBlock = images.get(index);
    if (Objects.nonNull(imageBlock)) {
      return imageBlock.y * IMAGE_SIZE;
    }

    log.error("invalid: index={}", index);
    return 0;
  }

}
