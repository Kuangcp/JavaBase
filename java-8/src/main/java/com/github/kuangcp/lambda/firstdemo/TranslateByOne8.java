package com.github.kuangcp.lambda.firstdemo;

import java.awt.*;
import java.util.function.Consumer;

/**
 * Created by Administrator on 2017/05/09
 * Kuangchengping@outlook.com
 */
public class TranslateByOne8 implements Consumer<Point> {
    @Override
    public void accept(Point point) {
        point.translate(1,1);
    }
}
