package com.github.kuangcp.tank.util;

import com.github.kuangcp.tank.constant.DirectType;
import com.github.kuangcp.tank.panel.TankGroundPanel;
import lombok.Getter;
import lombok.Setter;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-06 02:58
 * @see TankGroundPanel#keyPressed
 */
@Getter
@Setter
public class HoldingKeyEventMgr {

    public static HoldingKeyEventMgr instance = new HoldingKeyEventMgr();

    // 允许多键同时被触发
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

        pressMap.put(KeyEvent.VK_A, () -> this.left = true);
        pressMap.put(KeyEvent.VK_D, () -> this.right = true);
        pressMap.put(KeyEvent.VK_S, () -> this.down = true);
        pressMap.put(KeyEvent.VK_W, () -> this.up = true);

//        releaseMap.put(KeyEvent.VK_A, () -> this.direct = -1);
//        releaseMap.put(KeyEvent.VK_D, () -> this.direct = -1);
//        releaseMap.put(KeyEvent.VK_S, () -> this.direct = -1);
//        releaseMap.put(KeyEvent.VK_W, () -> this.direct = -1);

        releaseMap.put(KeyEvent.VK_J, () -> this.shot = false);
        releaseMap.put(KeyEvent.VK_CONTROL, () -> this.ctrl = false);

//        pressMap.put(KeyEvent.VK_A, () -> this.direct = DirectType.LEFT);
//        pressMap.put(KeyEvent.VK_D, () -> this.direct = DirectType.RIGHT);
//        pressMap.put(KeyEvent.VK_S, () -> this.direct = DirectType.DOWN);
//        pressMap.put(KeyEvent.VK_W, () -> this.direct = DirectType.UP);

        pressMap.put(KeyEvent.VK_CONTROL, () -> this.ctrl = true);
    }

    public void handleDirectPress(KeyEvent re) {
        Optional.ofNullable(pressMap.get(re.getKeyCode())).ifPresent(Runnable::run);
    }

    public void handleRelease(KeyEvent re) {
        Optional.ofNullable(releaseMap.get(re.getKeyCode())).ifPresent(Runnable::run);
    }

    public boolean hasPressMoveEvent() {
        return up || down || left || right;
//        return direct != DirectType.NONE;
    }

    public int getDirect() {
        if (up) {
            return DirectType.UP;
        } else if (right) {
            return DirectType.RIGHT;
        } else if (down) {
            return DirectType.DOWN;
        } else if (left) {
            return DirectType.LEFT;
        } else {
            return DirectType.NONE;
        }
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
