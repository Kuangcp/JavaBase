
package com.github.kuangcp.tank.domain;


import com.github.kuangcp.tank.constant.DirectType;
import com.github.kuangcp.tank.domain.robot.EnemyActionContext;
import com.github.kuangcp.tank.domain.robot.RobotRate;
import com.github.kuangcp.tank.domain.robot.RoundActionEnum;
import com.github.kuangcp.tank.util.TankTool;
import com.github.kuangcp.tank.util.executor.LoopEventExecutor;
import com.github.kuangcp.tank.v3.PlayStageMgr;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public Bullet s = null;
    public List<Bullet> bulletList = Collections.synchronizedList(new ArrayList<>());//子弹集合
    private long lastShotMs = 0;
    private long shotCDMs = 168;


    public static int maxLiveShot = 3; // 活跃子弹 最大数
    public boolean delayRemove = false; // 延迟回收内存，避免子弹线程执行中断
    public int moveRate;
    public int shotRate;

    public List<EnemyTank> ets;
    public List<Brick> bricks;
    public List<Iron> irons;

    boolean overlap = true; //同类之间允许重叠
    boolean bri = true;
    boolean ableMove = true;

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
        this.life = ThreadLocalRandom.current().nextInt(MAX_LIFE) + 1;
        this.speed = MAX_LIFE - life + 1;
        this.id = counter.addAndGet(1);

        this.setFixedDelayTime(40);
        this.moveRate = 2;
        this.shotRate = 17;

        this.afterBuild();
    }

    public EnemyTank(int x, int y, int speed, int direct) {
        super(x, y, speed);
        type = 1;
        this.direct = direct;
        this.speed = speed;
        this.alive = true;
        this.life = ThreadLocalRandom.current().nextInt(MAX_LIFE) + 1;
        this.id = counter.addAndGet(1);

        this.setFixedDelayTime(40);
        this.moveRate = 2;
        this.shotRate = 17;

        this.afterBuild();
    }

    private void afterBuild() {
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

    public void SetInfo(Hero hero, List<EnemyTank> ets, List<Brick> bricks, List<Iron> irons) {
        this.ets = ets;
        this.bricks = bricks;
        this.irons = irons;
    }

    public boolean isOverlap() {
        return overlap;
    }

    public void setOverlap(boolean overlap) {
        this.overlap = overlap;
    }

    public boolean isBri() {
        return bri;
    }

    public void setBri(boolean bri) {
        this.bri = bri;
    }

    @Override
    public void addLife(int delta) {
        super.addLife(delta);
        this.speed = MAX_LIFE - this.life + 1;
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
        if (this.bulletList.size() >= maxLiveShot || !this.isAlive()) {
            return;
        }

        switch (this.getDirect()) {
            case 0: {//0123 代表 上下左右
                s = new Bullet(this.getX() - 1, this.getY() - 15, 0);
                bulletList.add(s);
                break;
            }
            case 1: {
                s = new Bullet(this.getX() - 2, this.getY() + 15, 1);
                bulletList.add(s);
                break;
            }
            case 2: {
                s = new Bullet(this.getX() - 15 - 2, this.getY(), 2);
                bulletList.add(s);
                break;
            }
            case 3: {
                s = new Bullet(this.getX() + 15 - 2, this.getY() - 1, 3);
                bulletList.add(s);
                break;
            }
        }
        LoopEventExecutor.addLoopEvent(s);

        // 常规线程池
//        ExecutePool.shotPool.execute(s);
        // ForkJoin
//        ExecutePool.forkJoinPool.execute(s);
        // 协程池
//        ExecutePool.shotScheduler.getExecutor().execute(s);

        lastShotMs = nowMs;
    }

    //还是没有用，因为坦克太多，会卡住。。。
    public boolean TouchOther() {
        boolean flag = false;//没有碰到

        switch (this.direct) {
            case 0://上
                for (int i = 0; i < ets.size(); i++) {
                    EnemyTank et = ets.get(i);
                    if (et != this) {
                        if (et.direct == 0 || et.direct == 1) {//对方是上下
                            //自己的上左
                            if ((et.x - 10 <= this.x - 10 && et.x + 10 >= this.x - 10 &&
                                    et.y - 15 <= this.y - 15 && et.y + 15 >= this.y - 15)) {
                                flag = true;
                            }
                            //上右
                            if (et.x - 10 <= this.x + 10 && et.x + 10 >= this.x + 10 &&
                                    et.y - 15 <= this.y - 15 && et.y + 15 >= this.y - 15) {//判断最前方两个点是否在对方坦克区域内
                                flag = true;
                            }
                        }
                        if (et.direct == 2 || et.direct == 3) {//对方是左右
                            //自己的上左
                            if ((et.x - 15 <= this.x - 10 && et.x + 15 >= this.x - 10 &&
                                    et.y - 10 <= this.y - 15 && et.y + 10 >= this.y - 15)) {
                                flag = true;
                            }
                            //上右
                            if (et.x - 15 <= this.x + 10 && et.x + 15 >= this.x + 10 &&
                                    et.y - 10 <= this.y - 15 && et.y + 10 >= this.y - 15) {//判断最前方两个点是否在对方坦克区域内
                                flag = true;
                            }
                        }
                    }
                }
                break;
            case 1:
                for (int i = 0; i < ets.size(); i++) {
                    EnemyTank et = ets.get(i);
                    if (et != this) {
                        //对方是上下
                        if (et.direct == 0 || et.direct == 1) {
                            //自己的下左
                            if ((et.x - 10 <= this.x - 10 && et.x + 10 >= this.x - 10 &&
                                    et.y - 15 <= this.y + 15 && et.y + 15 >= this.y + 15)) {
                                flag = true;
                            }
                            //下右
                            if (et.x - 10 <= this.x + 10 && et.x + 10 >= this.x + 10 &&
                                    et.y - 15 <= this.y + 15 && et.y + 15 >= this.y + 15) {//判断最前方两个点是否在对方坦克区域内
                                flag = true;
                            }
                        }
                        //对方是左右
                        if (et.direct == 2 || et.direct == 3) {
                            //自己的下左
                            if ((et.x - 15 <= this.x - 10 && et.x + 15 >= this.x - 10 &&
                                    et.y - 10 <= this.y + 15 && et.y + 10 >= this.y + 15)) {
                                flag = true;
                            }
                            //下右
                            if (et.x - 15 <= this.x + 10 && et.x + 15 >= this.x + 10 &&
                                    et.y - 10 <= this.y + 15 && et.y + 10 >= this.y + 15) {//判断最前方两个点是否在对方坦克区域内
                                flag = true;
                            }
                        }
                    }
                }
                break;
            case 2:
                for (int i = 0; i < ets.size(); i++) {
                    EnemyTank et = ets.get(i);
                    if (et != this) {
                        if (et.direct == 0 || et.direct == 1) {
                            //对方是上下
                            //自己的左上
                            if ((et.x - 10 <= this.x - 15 && et.x + 10 >= this.x - 15 &&
                                    et.y - 15 <= this.y - 10 && et.y + 15 >= this.y - 10)) {
                                flag = true;
                            }
                            //右下
                            if (et.x - 10 <= this.x - 15 && et.x + 10 >= this.x - 15 &&
                                    et.y - 15 <= this.y + 10 && et.y + 15 >= this.y + 10) {//判断最前方两个点是否在对方坦克区域内
                                flag = true;
                            }
                        }
                        if (et.direct == 2 || et.direct == 3) {
                            //对方是左右
                            //自己的左上
                            if ((et.x - 15 <= this.x - 15 && et.x + 15 >= this.x - 15 &&
                                    et.y - 10 <= this.y - 10 && et.y + 10 >= this.y - 10)) {
                                flag = true;
                            }
                            //右下
                            if (et.x - 15 <= this.x - 15 && et.x + 15 >= this.x - 15 &&
                                    et.y - 10 <= this.y + 10 && et.y + 10 >= this.y + 10) {//判断最前方两个点是否在对方坦克区域内
                                flag = true;
                            }
                        }
                    }
                }
                break;
            case 3:
                for (int i = 0; i < ets.size(); i++) {
                    EnemyTank et = ets.get(i);
                    if (et != this) {
                        if (et.direct == 0 || et.direct == 1) {
                            //对方是上下
                            //自己的左上
                            if ((et.x - 10 <= this.x + 15 && et.x + 10 >= this.x + 15 &&
                                    et.y - 15 <= this.y - 10 && et.y + 15 >= this.y - 10)) {
                                flag = true;
                            }
                            //右下
                            if (et.x - 10 <= this.x + 15 && et.x + 10 >= this.x + 15 &&
                                    et.y - 15 <= this.y + 10 && et.y + 15 >= this.y + 10) {//判断最前方两个点是否在对方坦克区域内
                                flag = true;
                            }
                        }
                        if (et.direct == 2 || et.direct == 3) {
                            //对方是左右
                            //自己的左上
                            if ((et.x - 15 <= this.x + 15 && et.x + 15 >= this.x + 15 &&
                                    et.y - 10 <= this.y - 10 && et.y + 10 >= this.y - 10)) {
                                flag = true;
                            }
                            //右下
                            if (et.x - 15 <= this.x + 15 && et.x + 15 >= this.x + 15 &&
                                    et.y - 10 <= this.y + 10 && et.y + 10 >= this.y + 10) {//判断最前方两个点是否在对方坦克区域内
                                flag = true;
                            }
                        }
                    }
                }
                break;
        }

        return flag;
    }

    //只是移动一步的函数
    public boolean toUp() {
        if (y > 30) {
            ableMove = true;
            for (EnemyTank et : ets) {
                if (!TankTool.ablePass(this, et)) {
                    ableMove = (false);
                    break;
                }
            }
            for (Brick brick : bricks) {
                if (TankTool.ablePass(this, brick))
                    ableMove = false;
            }
            for (Iron iron : irons) {
                if (TankTool.ablePass(this, iron))
                    ableMove = false;
            }
            if (ableMove && TankTool.ablePass(this, PlayStageMgr.instance.hero)) {
                y -= speed;
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    log.error("", e);
                }
            } else {
                return false;
            }

        } else return false;
        return true;
    }

    public boolean toDown() {
        if (y < 530) {
            ableMove = true;
            for (EnemyTank et : ets) {
                if (!TankTool.ablePass(this, et)) {
                    ableMove = (false);
                    break;
                }
            }
            for (Brick brick : bricks) {
                if (TankTool.ablePass(this, brick))
                    ableMove = false;
            }
            for (Iron iron : irons) {
                if (TankTool.ablePass(this, iron))
                    ableMove = false;
            }
            if (ableMove && TankTool.ablePass(this, PlayStageMgr.instance.hero)) {
                y += speed;
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    log.error("", e);
                }
            } else {
                return false;
//    			this.direct = ((int)(Math.random()*100))%4;
            }

        } else return false;
        return true;
    }

    public boolean toLeft() {
        if (x > 30) {
            ableMove = true;
            for (EnemyTank et : ets) {
                if (!TankTool.ablePass(this, et)) {
                    ableMove = false;
                    break;
                }
            }
            for (Brick brick : bricks) {
                if (TankTool.ablePass(this, brick))
                    ableMove = false;
            }
            for (Iron iron : irons) {
                if (TankTool.ablePass(this, iron))
                    ableMove = false;
            }
            if (ableMove && TankTool.ablePass(this, PlayStageMgr.instance.hero)) {
                x -= speed;
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    log.error("", e);
                }
            } else {
                return true;
            }

        } else return false;
        return true;
    }

    public boolean toRight() {
        if (x < 710) {
            ableMove = true;
            for (EnemyTank et : ets) {
                if (!TankTool.ablePass(this, et)) {
                    ableMove = (false);
                    break;
                }
            }
            for (Brick brick : bricks) {
                if (TankTool.ablePass(this, brick))
                    ableMove = false;
            }
            for (Iron iron : irons) {
                if (TankTool.ablePass(this, iron))
                    ableMove = false;
            }
            if (ableMove && TankTool.ablePass(this, PlayStageMgr.instance.hero)) {
                x += speed;
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    log.error("", e);
                }
            } else {
                return false;
//    			this.direct = ((int)(Math.random()*100))%4;
            }

        } else return false;
        return true;
    }

    //重写
    int min = 0;

//    @Override
//    public void run() {
////        actionModeRun();
//        run2();
//
////        log.info("enemy die");
//
//        if (this.isAbort()) {
//            for (Bullet d : this.bulletList) {
//                d.alive = false;
//            }
//        }
//    }


    @Override
    public void run() {
//        newEventRun();
        moveOrShot();
    }

    // 运动
    public void actionModeRun() {
        while (true) {
            try {
                if (this.speed == 0) {
                    TankTool.yieldMsTime(1000);
                    continue;
                }
                this.direct = (int) (Math.random() * 4);
                switch (this.direct) {
                    case DirectType.UP:
                        toUp();
                        break;
                    case DirectType.DOWN:
                        toDown();
                        break;
                    case DirectType.LEFT:
                        toLeft();
                        break;
                    case DirectType.RIGHT:
                        toRight();
                        break;
                    default:
                        break;
                }

                //判断坦克是否死亡
                if (!this.isAlive()) {
                    //让坦克退出while即退出线程

                    if (!abort) {
                        PlayStageMgr.instance.hero.addPrize(1);
                    }
                    break;
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    private final EnemyActionContext actionContext = new EnemyActionContext();

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
        if (actionContext.getSameDirectCounter() > actionContext.getCurRoundStep()
                || !PlayStageMgr.instance.willInBorder(this)) {
            this.direct = DirectType.turnSelection(this.direct)[ThreadLocalRandom.current().nextInt(3)];
            actionContext.reset();
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
            default:
                log.warn("not exist direct");
                break;
        }
    }

    public void run2() {
        //子弹发射的（时间）坐标间隔

        while (true) {
            min++;
            if (min > 10000) min -= 10000;
//			System.out.println("x = "+this.x+" y = "+this.y);
//			System.out.println(speed);  终于找出错误，speed没有初始化

            //让坦克随机产生一个随机方向
            if (this.speed != 0) this.direct = (int) (Math.random() * 4);

            switch (this.direct) {//上下左右
                case 0://说明坦克正在向上移动
                    for (int i = 0; i < 30; i++) {
                        min++;
//					if(!bri)this.direct = (int)(Math.random()*4);
                        if (y > 30) {
                            if (TankTool.ablePass(this, PlayStageMgr.instance.hero) && overlap) y -= speed;
//						    if(bri)y-=speed;
//						    else {y+=speed;this.direct = 1;}
//						else continue;
                            else break;//这样才不会有敌人坦克凑在你附近不动
//						else this.direct = (int)(Math.random()*4);
                            if (min % 27 == 0)
                                this.finalShotAction();
                            TankTool.yieldMsTime(50);
                        }

                    }

                    break;
                case 1:
                    for (int i = 0; i < 30; i++) {
                        min++;
//					if(!bri)this.direct = (int)(Math.random()*4);

                        if (y < 530) {
                            if (TankTool.ablePass(this, PlayStageMgr.instance.hero) && overlap && bri) y += speed;
//						    if(bri) y+=speed;
//						    else {y-=speed;this.direct = 0;}
//						else continue;
                            else break;
                            if (min % 27 == 0) {
                                this.finalShotAction();
                            }
                            TankTool.yieldMsTime(50);
                        }

                    }

                    break;
                case 2:
                    for (int i = 0; i < 30; i++) {
                        min++;
//					if(!bri)this.direct = (int)(Math.random()*4);

                        if (x > 30) {
                            if (TankTool.ablePass(this, PlayStageMgr.instance.hero) && overlap && bri) x -= speed;
//						    if(bri)x-=speed;
//						    else{x+=speed;this.direct = 3;}
//						else continue;
                            else break;
                            if (min % 27 == 0)
                                this.finalShotAction();
                            TankTool.yieldMsTime(50);
                        }

                    }

                    break;
                case 3:
                    for (int i = 0; i < 30; i++) {
                        min++;
//					if(!bri)this.direct = (int)(Math.random()*4);

                        if (x < 710) {
                            if (TankTool.ablePass(this, PlayStageMgr.instance.hero) && overlap && bri) {
                                x += speed;
                            }
//						     if(bri)x+=speed;
//						     else{x-=speed;this.direct = 2;}
                            else break;
//						else continue;
                            if (min % 27 == 0)
                                this.finalShotAction();

                            TankTool.yieldMsTime(50);
                        }


                    }
                    break;
                default:
                    break;
            }

            //判断坦克是否死亡
            if (!this.isAlive()) {
                //让坦克退出while即退出线程
                if (!abort) {
                    PlayStageMgr.instance.hero.addPrize(1);
                }

                break;
            }
        }
    }


    public void newEventRun() {
        if (PlayStageMgr.pause) {
            return;
        }
        //判断坦克是否死亡
        if (!this.isAlive()) {
            this.stop();
            return;
        }

        //子弹发射的（时间）坐标间隔
        min++;
        if (min > 10000) min -= 10000;
//			System.out.println("x = "+this.x+" y = "+this.y);
//			System.out.println(speed);  终于找出错误，speed没有初始化

        //让坦克随机产生一个随机方向
        if (this.speed != 0) this.direct = (int) (Math.random() * 4);

        switch (this.direct) {//上下左右
            case 0://说明坦克正在向上移动
                for (int i = 0; i < 5; i++) {
                    min++;
//					if(!bri)this.direct = (int)(Math.random()*4);
                    if (y > 30) {
                        if (PlayStageMgr.ablePassByHero(this) && overlap) y -= speed;
//						    if(bri)y-=speed;
//						    else {y+=speed;this.direct = 1;}
//						else continue;
                        else break;//这样才不会有敌人坦克凑在你附近不动
//						else this.direct = (int)(Math.random()*4);
                        if (min % 27 == 0)
                            this.finalShotAction();
//                        TankTool.yieldMsTime(50);
                    }

                }

                break;
            case 1:
                for (int i = 0; i < 5; i++) {
                    min++;
//					if(!bri)this.direct = (int)(Math.random()*4);

                    if (y < 530) {
                        if (PlayStageMgr.ablePassByHero(this) && overlap && bri) y += speed;
//						    if(bri) y+=speed;
//						    else {y-=speed;this.direct = 0;}
//						else continue;
                        else break;
                        if (min % 27 == 0) {
                            this.finalShotAction();
                        }
//                        TankTool.yieldMsTime(50);
                    }

                }

                break;
            case 2:
                for (int i = 0; i < 5; i++) {
                    min++;
//					if(!bri)this.direct = (int)(Math.random()*4);

                    if (x > 30) {
                        if (PlayStageMgr.ablePassByHero(this) && overlap && bri) x -= speed;
//						    if(bri)x-=speed;
//						    else{x+=speed;this.direct = 3;}
//						else continue;
                        else break;
                        if (min % 27 == 0)
                            this.finalShotAction();
//                        TankTool.yieldMsTime(50);
                    }

                }

                break;
            case 3:
                for (int i = 0; i < 5; i++) {
                    min++;
//					if(!bri)this.direct = (int)(Math.random()*4);

                    if (x < 710) {
                        if (PlayStageMgr.ablePassByHero(this) && overlap && bri) {
                            x += speed;
                        }
//						     if(bri)x+=speed;
//						     else{x-=speed;this.direct = 2;}
                        else break;
//						else continue;
                        if (min % 27 == 0)
                            this.finalShotAction();

//                        TankTool.yieldMsTime(50);
                    }
                }
                break;
            default:
                break;
        }
    }
}
