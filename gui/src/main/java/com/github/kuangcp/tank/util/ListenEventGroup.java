package com.github.kuangcp.tank.util;

import com.github.kuangcp.tank.panel.TankGroundPanel;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author https://github.com/kuangcp on 2021-09-06 02:58
 * @see TankGroundPanel#keyPressed
 */
public class ListenEventGroup {

    public static ListenEventGroup instance = new ListenEventGroup();

    private volatile boolean up;
    private volatile boolean down;
    private volatile boolean left;
    private volatile boolean right;
    private volatile boolean shot;

    private final Map<Integer, Runnable> actionMap = new HashMap<>();

    public ListenEventGroup() {
        actionMap.put(KeyEvent.VK_A, () -> this.left = false);
        actionMap.put(KeyEvent.VK_D, () -> this.right = false);
        actionMap.put(KeyEvent.VK_S, () -> this.down = false);
        actionMap.put(KeyEvent.VK_W, () -> this.up = false);
        actionMap.put(KeyEvent.VK_SPACE, () -> this.shot = false);
    }

    public void handleRelease(KeyEvent re) {
        Optional.ofNullable(actionMap.get(re.getKeyCode())).ifPresent(Runnable::run);
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
