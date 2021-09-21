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

    private String[] imgPathArr;

    // 实际渲染大小
    public int width;
    public int height;

    public void loadImg() {
        Object val = PropertiesMgr.imgProperties.get(this.getConfigKey());
        imgPathArr = val.toString().split(",");

        if (Objects.isNull(imgPathArr) || imgPathArr.length < 1) {
            log.warn("no image {}", this.getClass().getSimpleName());
            return;
        }

        try {
            this.imgArr = new Image[imgPathArr.length];
            for (int i = 0; i < imgPathArr.length; i++) {
                this.imgArr[i] = ImageIO.read(getClass().getResourceAsStream(imgPathArr[i]));
            }
        } catch (IOException e) {
            log.error("", e);
        }
        curImg = this.imgArr[(int) (Math.random() * imgPathArr.length)];
    }

    public abstract String getConfigKey();
}
