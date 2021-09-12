package com.github.kuangcp.tank.resource;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

/**
 * @author https://github.com/kuangcp on 2021-09-13 01:17
 */
@Slf4j
public abstract class AbstractImgListMgr {

    public Image curImg = null;
    public Image[] imgArr = null;

    public void loadImg() {
        final String[] imgArr = getImgPathArr();
        if (Objects.isNull(imgArr) || imgArr.length < 1) {
            log.warn("no image");
            return;
        }
        try {
            this.imgArr = new Image[imgArr.length];
            for (int i = 0; i < imgArr.length; i++) {
                this.imgArr[i] = ImageIO.read(getClass().getResourceAsStream(imgArr[i]));
            }
        } catch (IOException e) {
            log.error("", e);
        }
        curImg = this.imgArr[(int) (Math.random() * imgArr.length)];
    }

    public abstract String[] getImgPathArr();

}
