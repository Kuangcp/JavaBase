package com.github.kuangcp.tank.v1;

/**
 * 最起初的坦克类
 * 往后有继承
 */
public class Tank {
    int x;          // 坦克中心的横坐标
    int y;          // 坦克中心的纵坐标
    int direct = 0;   // 初始方向
    int type = 0;     // 坦克的种类
    int speed = 5;      // 前进的步长
    boolean alive = true;//是否存活
    int life = 1;//生命值

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean isLive) {
        this.alive = isLive;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int s) {
        speed = s;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
//	判断子弹是否击中坦克
//	public void Bong(){
//		
//	}

    //  构造器
    public Tank(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }
}
