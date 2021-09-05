package com.github.kuangcp.tank.v3;

/**
 * 1 准备好图片
 * 2 定义这个类
 * 3 击中坦克的时候创建爆炸对象，加入集合
 * <p>
 * 如果坐标在变  最好做成一个线程
 * 用一个变量来规定图片放映的顺序及是否放映结束 结束了就可以从集合中删除
 * 不得不说 思路的确挺好
 */
public class Bomb {

    int bx, by;
    //炸弹的生命
    int life = 15;
    boolean isLive = true;

    public Bomb(int bx, int by) {
        this.bx = bx;
        this.by = by;
    }

    public Bomb() {
    }

    //减少生命值
    public void lifeDown() {
        if (life > 0) {
            life--;
        } else {

            this.isLive = false;
        }
    }
}
