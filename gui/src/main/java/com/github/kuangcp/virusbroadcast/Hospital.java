package com.github.kuangcp.virusbroadcast;

import com.github.kuangcp.virusbroadcast.constant.Constants;
import com.github.kuangcp.virusbroadcast.domain.Bed;
import com.github.kuangcp.virusbroadcast.domain.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public enum Hospital {

  INSTANCE;

  private int x = 800;
  private int y = 110;
  private int width;
  private int height = 606;
  private List<Bed> beds = new ArrayList<>();

  Hospital() {
    if (Constants.BED_COUNT == 0) {
      width = 0;
      height = 0;
    }
    int column = Constants.BED_COUNT / 100;
    width = column * 6;

    Point offset = new Point(800, 100);
    for (int i = 0; i < column; i++) {
      for (int j = 10; j <= 610; j += 6) {
        Bed bed = new Bed(offset.getX() + i * 6, offset.getY() + j);
        beds.add(bed);
      }
    }
  }

  public Bed pickBed() {
    for (Bed bed : beds) {
      if (bed.isEmpty()) {
        return bed;
      }
    }
    return null;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }
}
