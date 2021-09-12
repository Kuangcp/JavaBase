package com.github.kuangcp.tank.resource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author https://github.com/kuangcp on 2021-09-11 16:41
 */
@Slf4j
public class AvatarImgMgr extends AbstractImgListMgr {

    public static AvatarImgMgr instance = new AvatarImgMgr();

    public AvatarImgMgr() {
        super.width = 60;
        super.height = 60;

        super.imgPathArr = new String[]{"/images/Me.jpg", "/images/Me2.jpg", "/images/Me3.jpg", "/images/Me4.jpg",
                "/images/Me5.jpg"};
    }
}
