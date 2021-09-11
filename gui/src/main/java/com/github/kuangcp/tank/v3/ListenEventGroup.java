package com.github.kuangcp.tank.v3;

import java.awt.event.KeyEvent;

/**
 * @author https://github.com/kuangcp on 2021-09-06 02:58
 * @see MyPanel3#keyPressed
 */
public class ListenEventGroup {

    public static ListenEventGroup instance = new ListenEventGroup();

    private volatile boolean up;
    private volatile boolean down;
    private volatile boolean left;
    private volatile boolean right;
    private volatile boolean shot;

    public void handleRelease(KeyEvent re){
        switch (re.getKeyCode()) {
            case KeyEvent.VK_A:
                this.setLeft(false);
                break;
            case KeyEvent.VK_D:
                this.setRight(false);
                break;
            case KeyEvent.VK_S:
                this.setDown(false);
                break;
            case KeyEvent.VK_W:
                this.setUp(false);
                break;
            case KeyEvent.VK_SPACE:
                this.setShot(false);
                break;
            default:
                break;
        }
    }
    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isShot() {
        return shot;
    }

    public void setShot(boolean shot) {
        this.shot = shot;
    }

    public boolean hasPressMoveEvent() {
        return up || down || left || right;
    }

    @Override
    public String toString() {
        return "ListenEventGroup{" +
                "up=" + up +
                ", down=" + down +
                ", left=" + left +
                ", right=" + right +
                ", shot=" + shot +
                '}';
    }
}
