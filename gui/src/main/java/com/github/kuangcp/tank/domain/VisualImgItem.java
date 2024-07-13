package com.github.kuangcp.tank.domain;

import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2024-07-13 20:21
 */
public interface VisualImgItem {

    void drawSelf(Graphics g, ImageObserver observer);
}
