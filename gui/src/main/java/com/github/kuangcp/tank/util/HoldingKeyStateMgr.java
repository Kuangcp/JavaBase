package com.github.kuangcp.tank.util;

import com.github.kuangcp.tank.constant.DirectType;
import com.github.kuangcp.tank.panel.TankGroundPanel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-06 02:58
 * @see TankGroundPanel#keyPressed 触发
 */
@Getter
@Setter
@Slf4j
public class HoldingKeyStateMgr {

    private long lastEventTick = 0;
    public static final HoldingKeyStateMgr instance = new HoldingKeyStateMgr();

    // 允许多键同时被触发
    private volatile boolean up;
    private volatile boolean down;
    private volatile boolean left;
    private volatile boolean right;

    private volatile boolean shot;

    private volatile boolean ctrl;

    private final Map<Integer, Runnable> releaseMap = new HashMap<>();
    private final Map<Integer, Runnable> pressMap = new HashMap<>();

    public HoldingKeyStateMgr() {

        releaseMap.put(KeyEvent.VK_A, () -> this.left = false);
        releaseMap.put(KeyEvent.VK_D, () -> this.right = false);
        releaseMap.put(KeyEvent.VK_S, () -> this.down = false);
        releaseMap.put(KeyEvent.VK_W, () -> this.up = false);

        pressMap.put(KeyEvent.VK_A, () -> this.left = true);
        pressMap.put(KeyEvent.VK_D, () -> this.right = true);
        pressMap.put(KeyEvent.VK_S, () -> this.down = true);
        pressMap.put(KeyEvent.VK_W, () -> this.up = true);


        releaseMap.put(KeyEvent.VK_J, () -> this.shot = false);
        releaseMap.put(KeyEvent.VK_CONTROL, () -> this.ctrl = false);

        pressMap.put(KeyEvent.VK_CONTROL, () -> this.ctrl = true);
    }

    public void handleDirectPress(KeyEvent re) {
        handleDirectPress(re.getKeyCode());
    }

    public void handleRelease(KeyEvent re) {
        handleRelease(re.getKeyCode());
    }

    public void handleDirectPress(Integer code) {
        Optional.ofNullable(pressMap.get(code)).ifPresent(Runnable::run);
    }

    public void handleRelease(Integer code) {
        Optional.ofNullable(releaseMap.get(code)).ifPresent(Runnable::run);
    }

    // 前端 ws 采样定时调用
    public void handleJoy(Integer code) {
        if (Objects.isNull(code)) {
            return;
        }
        final long now = System.currentTimeMillis();
//        if (lastEventTick != 0 && now - lastEventTick < 100) {
//            return;
//        }
        lastEventTick = now;
        this.left = false;
        this.right = false;
        this.up = false;
        this.down = false;

        if (code == DirectType.NONE) {
            return;
        }

        switch (code) {
            case DirectType.UP:
                this.up = true;
                break;
            case DirectType.DOWN:
                this.down = true;
                break;
            case DirectType.LEFT:
                this.left = true;
                break;
            case DirectType.RIGHT:
                this.right = true;
                break;
        }
//        log.info("code={} left={} right={}", code, this.left, this.right);
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
