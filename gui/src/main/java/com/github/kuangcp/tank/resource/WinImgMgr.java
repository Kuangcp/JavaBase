package com.github.kuangcp.tank.resource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author https://github.com/kuangcp on 2021-09-13 01:13
 */
@Slf4j
public class WinImgMgr extends AbstractImgListMgr {

    public static WinImgMgr instance = new WinImgMgr();

    public String[] imgPathArr = new String[]{"/images/Win2.jpg"};

    @Override
    public String[] getImgPathArr() {
        return imgPathArr;
    }
}
