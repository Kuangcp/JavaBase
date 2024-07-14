package com.github.kuangcp.tank.resource;

import com.github.kuangcp.tank.domain.StageBorder;
import com.github.kuangcp.tank.domain.VisualImgItem;
import com.github.kuangcp.tank.mgr.PlayStageMgr;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-11 16:41
 */
@Slf4j
public class AvatarImgMgr extends AbstractImgListMgr implements VisualImgItem {

    public static final AvatarImgMgr instance = new AvatarImgMgr();

    public AvatarImgMgr() {
        super.width = 60;
        super.height = 60;
    }

    public String getConfigKey() {
        return PropertiesMgr.Key.Img.ROUND_AVATAR;
    }

    @Override
    public void drawSelf(Graphics g, ImageObserver observer) {
        final StageBorder border = PlayStageMgr.instance.border;
        g.drawImage(instance.curImg, border.getHomeX(), border.getHomeY(), instance.width, instance.height, observer);
    }
}
