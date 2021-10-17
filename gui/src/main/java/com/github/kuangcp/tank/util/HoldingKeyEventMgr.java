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
public class HoldingKeyEventMgr {

    public static HoldingKeyEventMgr instance = new HoldingKeyEventMgr();

    private volatile boolean up;
    private volatile boolean down;
    private volatile boolean left;
    private volatile boolean right;
    private volatile boolean shot;

    private volatile boolean ctrl;

    private final Map<Integer, Runnable> releaseMap = new HashMap<>();
    private final Map<Integer, Runnable> pressMap = new HashMap<>();

    public HoldingKeyEventMgr() {
        releaseMap.put(KeyEvent.VK_A, () -> this.left = false);
        releaseMap.put(KeyEvent.VK_D, () -> this.right = false);
        releaseMap.put(KeyEvent.VK_S, () -> this.down = false);
        releaseMap.put(KeyEvent.VK_W, () -> this.up = false);
        releaseMap.put(KeyEvent.VK_J, () -> this.shot = false);

        pressMap.put(KeyEvent.VK_A, () -> this.left = true);
        pressMap.put(KeyEvent.VK_D, () -> this.right = true);
        pressMap.put(KeyEvent.VK_S, () -> this.down = true);
        pressMap.put(KeyEvent.VK_W, () -> this.up = true);
    }

    public void handleDirectPress(KeyEvent re){
        Optional.ofNullable(pressMap.get(re.getKeyCode())).ifPresent(Runnable::run);
    }
    public void handleRelease(KeyEvent re) {
        Optional.ofNullable(releaseMap.get(re.getKeyCode())).ifPresent(Runnable::run);
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
