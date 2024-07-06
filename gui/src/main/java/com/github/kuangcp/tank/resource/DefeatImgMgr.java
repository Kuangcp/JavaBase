package com.github.kuangcp.tank.resource;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-13 01:32
 */
public class DefeatImgMgr extends AbstractImgListMgr {

    public static DefeatImgMgr instance = new DefeatImgMgr();

    public DefeatImgMgr() {
        super.width = 760;
        super.height = 650;
    }
    
    public String getConfigKey(){
        return PropertiesMgr.Key.Img.ROUND_DEFEAT;
    }
}
