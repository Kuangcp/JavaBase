package com.github.kuangcp.tank.resource;

/**
 * @author https://github.com/kuangcp on 2021-09-13 01:32
 */
public class OverImgMgr extends AbstractImgListMgr {

    public static OverImgMgr instance = new OverImgMgr();

    public String[] imgPathArr = new String[]{"/images/Over2.jpg", "/images/Over4.jpg"};

    @Override
    public String[] getImgPathArr() {
        return imgPathArr;
    }
}
