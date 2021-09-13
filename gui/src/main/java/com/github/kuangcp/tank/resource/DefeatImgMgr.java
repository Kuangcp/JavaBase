package com.github.kuangcp.tank.resource;

/**
 * @author https://github.com/kuangcp on 2021-09-13 01:32
 */
public class DefeatImgMgr extends AbstractImgListMgr {

    public static DefeatImgMgr instance = new DefeatImgMgr();

    public DefeatImgMgr() {
        super.imgPathArr = new String[]{"/images/Over2.jpg", "/images/Over4.jpg"};

        super.width = 760;
        super.height = 650;
    }
}
