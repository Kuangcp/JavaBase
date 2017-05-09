package com.lambda.firstdemo;

import java.awt.*;

/**
 * Created by Administrator on 2017/05/09
 * Kuangchengping@outlook.com
 */
public class TranslateByOne implements PointAction {
    @Override
    public void doForPoint(Point point) {
        point.translate(1,1);
    }
}
