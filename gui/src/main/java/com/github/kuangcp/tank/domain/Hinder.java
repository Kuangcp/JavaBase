package com.github.kuangcp.tank.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 障碍物的最基本类
 */

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class Hinder extends AnyLife {

    int hx, hy; // 障碍物绘图坐标  左上角顶点 的坐标
    int width = 20;
    int height = 10;

    public Hinder(int hx, int hy) {
        alive = true;
        this.hx = hx;
        this.hy = hy;
    }



}
