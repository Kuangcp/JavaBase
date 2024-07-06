package com.github.kuangcp.tank.resource;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-13 01:17
 */
@Slf4j
public abstract class AbstractImgListMgr {

    public Image curImg = null;
    public Image[] imgArr = null;

    // 实际渲染大小
    public int width;
    public int height;

    public void loadImg() throws IOException {
        String val = PropertiesMgr.imgProperties.getProperty(this.getConfigKey());
        String[] imgPathArr = val.split(",");

        if (imgPathArr.length < 1) {
            log.warn("no image {}", this.getClass().getSimpleName());
            return;
        }

        this.imgArr = new Image[imgPathArr.length];
        for (int i = 0; i < imgPathArr.length; i++) {
            this.imgArr[i] = ImageIO.read(getClass().getResourceAsStream(imgPathArr[i]));
        }

        curImg = this.imgArr[(int) (Math.random() * imgPathArr.length)];
    }

    public abstract String getConfigKey();
}
