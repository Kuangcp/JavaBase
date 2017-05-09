package com.lambda.firstdemo;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/05/09
 * Kuangchengping@outlook.com
 */
public class PointArrayList extends ArrayList<Point> {
    public void forEach(PointAction pointAction){
        for (Point point :this) {
            pointAction.doForPoint(point);
        }

    }
}
