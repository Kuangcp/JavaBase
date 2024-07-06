package com.github.kuangcp.tank.domain;

import com.github.kuangcp.tank.constant.DirectType;
import com.github.kuangcp.tank.domain.robot.EnemyActionContext;
import com.github.kuangcp.tank.domain.robot.RobotRate;
import com.github.kuangcp.tank.domain.robot.RoundActionEnum;
import com.github.kuangcp.tank.util.executor.LoopEventExecutor;
import com.github.kuangcp.tank.mgr.PlayStageMgr;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 敌方坦克父类
 * 可以继续延伸做出多样化的坦克
 */
@Slf4j
public class EnemyTank extends Tank implements Runnable, RobotRate {

    private static final AtomicLong counter = new AtomicLong();

    public final long id;
    public List<Bullet> bulletList = new CopyOnWriteArrayList<>();//子弹集合
    private long lastShotMs = 0;
    private long shotCDMs = 168;

    public int INIT_MAX_LIVE_BULLET = 4;
    public int maxLiveBullet = INIT_MAX_LIVE_BULLET + 1; // 活跃子弹 最大数

    private final EnemyActionContext actionContext = new EnemyActionContext();

    /**
     * 延迟回收内存（标记使用一次）
     * 避免子弹线程执行中断，突然消失
     */
    public boolean delayRemove = false;
    /**
     * 移动频率
     */
    public int moveRate;
    /**
     * 发射子弹频率
     */
    public int shotRate;

    /**
     * 随机生成 的 最大生命值, 每个生命值都有对应的颜色
     *
     * @see EnemyTank#COLOR_HASH_MAP
     * @see EnemyTank#drawSelf
     */
    private static final int MAX_LIFE = 7;
    private static final Map<Integer, Color> COLOR_HASH_MAP = new HashMap<>(MAX_LIFE);

    static {
        COLOR_HASH_MAP.put(1, Color.WHITE);
        COLOR_HASH_MAP.put(2, new Color(93, 217, 41));
        COLOR_HASH_MAP.put(3, new Color(34, 155, 234));
        COLOR_HASH_MAP.put(4, new Color(155, 62, 202));
        COLOR_HASH_MAP.put(5, new Color(240, 57, 23));
        COLOR_HASH_MAP.put(6, new Color(240, 57, 23));
        COLOR_HASH_MAP.put(7, new Color(240, 57, 23));
    }

    public EnemyTank(int x, int y, int direct) {
        super(x, y, 0);
        type = 1;
        this.direct = direct;
        this.alive = true;

        this.randomLife();
        this.resetPropByLife();

        this.id = counter.addAndGet(1);

        this.setFixedDelayTime(40);
        this.moveRate = 2;
        this.shotRate = 7;

        this.registerDestroy();
    }

    private void registerDestroy() {
        this.registerHook(() -> {
            if (this.isAbort()) {
                for (Bullet d : this.bulletList) {
                    d.alive = false;
                }
            } else {
                PlayStageMgr.instance.hero.addPrize(1);
            }
        });
    }

    private void randomLife() {
        double val = ThreadLocalRandom.current().nextGaussian();
        val = val * Math.sqrt(2) + MAX_LIFE / 3.0;
        val = Math.min(Math.max(val, 1), MAX_LIFE);
        this.life = (int) val;
    }

    /**
     * 依据生命值重置属性(速度，子弹数)
     */
    private void resetPropByLife() {
        this.speed = Math.max((MAX_LIFE - life + 1) / 2, 1);
        if (this.life == 1) {
            this.speed++;
        }
        this.maxLiveBullet = INIT_MAX_LIVE_BULLET + this.life;
    }

    @Override
    public int getMoveRate() {
        return this.moveRate;
    }

    @Override
    public int getShotRate() {
        return this.shotRate;
    }

    @Override
    public void drawSelf(Graphics g) {
        g.setColor(COLOR_HASH_MAP.getOrDefault(this.life, Color.white));
        super.drawSelf(g);
    }

    @Override
    public void addLife(int delta) {
        super.addLife(delta);
        this.resetPropByLife();
    }

    /**
     * 发射子弹    函数
     */
    public void finalShotAction() {
        //判断坦克方向来 初始化子弹的起始发射位置
        final long nowMs = System.currentTimeMillis();
        if (lastShotMs != 0 && nowMs - lastShotMs < shotCDMs) {
            return;
        }
        if (this.bulletList.size() >= maxLiveBullet || !this.isAlive()) {
            return;
        }
        switch (this.getDirect()) {
            case DirectType.UP: {
                Bullet s = new Bullet(this.getX() - 1, this.getY() - 15, 0);
                bulletList.add(s);
                LoopEventExecutor.addLoopEvent(s);
                break;
            }
            case DirectType.DOWN: {
                Bullet s = new Bullet(this.getX() - 2, this.getY() + 15, 1);
                bulletList.add(s);
                LoopEventExecutor.addLoopEvent(s);
                break;
            }
            case DirectType.LEFT: {
                Bullet s = new Bullet(this.getX() - 15 - 2, this.getY(), 2);
                bulletList.add(s);
                LoopEventExecutor.addLoopEvent(s);
                break;
            }
            case DirectType.RIGHT: {
                Bullet s = new Bullet(this.getX() + 15 - 2, this.getY() - 1, 3);
                bulletList.add(s);
                LoopEventExecutor.addLoopEvent(s);
                break;
            }
        }

        // 常规线程池
//        ExecutePool.shotPool.execute(s);
        // ForkJoin
//        ExecutePool.forkJoinPool.execute(s);
        // 协程池
//        ExecutePool.shotScheduler.getExecutor().execute(s);

        lastShotMs = nowMs;
    }

    @Override
    public void run() {
        moveOrShot();
    }

    public void moveOrShot() {
        if (!this.isAlive()) {
            this.stop();
            return;
        }

        if (PlayStageMgr.pause || this.speed <= 0) {
            return;
        }

        final RoundActionEnum roundActionEnum = actionContext.roundAction(this);
//        log.info("mode={}", roundActionEnum);
        switch (roundActionEnum) {
            case MOVE:
                this.finalMoveAction();
                return;
            case SHOT:
                this.finalShotAction();
                return;
            case STAY:
                return;
            default:
                log.warn("not support");
        }
    }

    private void finalMoveAction() {
        final boolean sameDirect = actionContext.getSameDirectCounter() > actionContext.getCurRoundStep();
        final boolean ablePassHero = PlayStageMgr.ablePassByHero(this);
        final boolean ablePassHinder = PlayStageMgr.ablePassByHinder(this);

        // 重定向
        if (sameDirect || !ablePassHero || !ablePassHinder || !PlayStageMgr.instance.willInBorder(this)) {
            this.direct = DirectType.rollDirect(this.direct);
            actionContext.resetDirectCount();
            return;
        }

        switch (this.direct) {
            case DirectType.UP:
                y -= this.speed;
                actionContext.addCount();
                break;
            case DirectType.DOWN:
                y += this.speed;
                actionContext.addCount();
                break;
            case DirectType.LEFT:
                x -= this.speed;
                actionContext.addCount();
                break;
            case DirectType.RIGHT:
                x += this.speed;
                actionContext.addCount();
                break;
        }
    }
}
