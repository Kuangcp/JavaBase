package com.github.kuangcp.tank.resource;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 * @author https://github.com/kuangcp on 2021-09-11 16:41
 */
@Slf4j
public class AvatarMgr {

    public static AvatarMgr instance = new AvatarMgr();

    Image[] avatarArr = new Image[5];

    public Image curImg = null;

    public void initImg() {
        try {
            avatarArr[0] = ImageIO.read(getClass().getResourceAsStream("/images/Me.jpg"));
            avatarArr[1] = ImageIO.read(getClass().getResourceAsStream("/images/Me2.jpg"));
            avatarArr[2] = ImageIO.read(getClass().getResourceAsStream("/images/Me3.jpg"));
            avatarArr[3] = ImageIO.read(getClass().getResourceAsStream("/images/Me4.jpg"));
            avatarArr[4] = ImageIO.read(getClass().getResourceAsStream("/images/Me5.jpg"));
        } catch (IOException e) {
            log.error("", e);
        }
        curImg = avatarArr[(int) (Math.random() * 5)];
    }
}
