
package com.github.kuangcp.tank.domain;


import com.github.kuangcp.tank.constant.DirectType;
import com.github.kuangcp.tank.util.ExecutePool;
import com.github.kuangcp.tank.util.TankTool;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 敌方坦克父类
 * 可以继续延伸做出多样化的坦克
 */
@Slf4j
public class EnemyTank extends Tank implements Runnable {

    private static final AtomicLong counter = new AtomicLong();
    // 敌人 id
    public long id;

    public List<Shot> shotList = Collections.synchronizedList(new ArrayList<>());//子弹集合
    private long lastShotMs = 0;
    private long shotCDMs = 168;

    public List<EnemyTank> ets;
    public List<Brick> bricks;
    public List<Iron> irons;
    public Shot s = null;
    public static int maxLiveShot = 3; //子弹线程存活的最大数
    public boolean delayRemove = false;

    Hero hero;

    boolean overlap = true; //同类之间允许重叠
    boolean bri = true;
    boolean ableMove = true;

    //继承了属性，即使直接使用父类的构造器，构造器也一定要显式声明
    public EnemyTank(int x, int y, int speed, int direct) {
        super(x, y, speed);
        type = 1;
        this.direct = direct;
        this.speed = speed;
        this.alive = true;
        this.life = ThreadLocalRandom.current().nextInt(3) + 1;
        this.id = counter.addAndGet(1);
//        log.info("create new Demons. {}", id);
    }

    public void SetInfo(Hero hero, List<EnemyTank> ets, List<Brick> bricks, List<Iron> irons) {
        this.hero = hero;
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

    /**
     * 视频上的思想是 在panel上写一个工具函数 形参是 子弹和坦克
     * 把函数放在 Run函数内跑
     * 遮掩更显得逻辑性强一些，代码复用率高一点
     * @param hero
     */

    /**
     * 发射子弹    函数
     */
    public void shotEnemy() {
        //判断坦克方向来 初始化子弹的起始发射位置
        final long nowMs = System.currentTimeMillis();
        if (lastShotMs != 0 && nowMs - lastShotMs < shotCDMs) {
            return;
        }
        if (this.shotList.size() >= this.maxLiveShot || !this.isAlive()) {
            return;
        }

        switch (this.getDirect()) {
            case 0: {//0123 代表 上下左右
                s = new Shot(this.getX() - 1, this.getY() - 15, 0);
                shotList.add(s);
                break;
            }
            case 1: {
                s = new Shot(this.getX() - 2, this.getY() + 15, 1);
                shotList.add(s);
                break;
            }
            case 2: {
                s = new Shot(this.getX() - 15 - 2, this.getY(), 2);
                shotList.add(s);
                break;
            }
            case 3: {
                s = new Shot(this.getX() + 15 - 2, this.getY() - 1, 3);
                shotList.add(s);
                break;
            }
        }
        //启动子弹线程
        ExecutePool.shotPool.execute(s);
//        ExecutePool.forkJoinPool.execute(s);
//        ExecutePool.shotScheduler.getExecutor().execute(s);

        lastShotMs = nowMs;
    }

//    public void run (){
////    	synchronized (this) {
//    		while(true){
//        		this.direct = ((int)(Math.random()*100))%4;
////        		System.out.println(this.direct);
//
//        		switch(this.getDirect()){
//            	case 0:{
//            		for(int i=0;i<8;i++) if(this.toUp());else break;
////            		System.out.println(this.direct);
//            		if(this.y%25==0)this.shotEnemy();
//            		break;
//            	}
//            	case 1:{
//            		for(int i=0;i<8;i++) if(this.toDown());else break;
//            		if(this.y%25==0)this.shotEnemy();
//            		break;
//            	}
//            	case 2:{
//            		for(int i=0;i<8;i++) if(this.toLeft());else break;
//            		if(this.y%25==0)this.shotEnemy();
//            		break;
//            	}
//            	case 3:{
//            		for(int i=0;i<8;i++) if(this.toRight());else break;
//            		if(this.y%25==0)this.shotEnemy();
//            		break;
//            		
//            	}
//            	default:
//    				break;
//            	}    		
//
////        		this.direct = ((int)(Math.random()*100))%4;
//
//            	if (!this.getisLive()){
//        			//让坦克退出while即退出线程
//        			hero.setPrize(hero.getPrize()+1);
//        			break;
//        		}
//        	}
////		} 
//    	
//    }


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
            if (ableMove && TankTool.ablePass(this, hero)) {
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
            if (ableMove && TankTool.ablePass(this, hero)) {
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
            if (ableMove && TankTool.ablePass(this, hero)) {
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
            if (ableMove && TankTool.ablePass(this, hero)) {
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

    @Override
    public void run() {
//        actionModeRun();
        run2();

        if (this.isAbort()) {
            for (Shot d : this.shotList) {
                d.isLive = false;
            }
        }
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
                    hero.addPrize(1);
                    break;
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    // 攻击
    public void actionModeAttack() {

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
                            if (TankTool.ablePass(this, hero) && overlap) y -= speed;
//						    if(bri)y-=speed;
//						    else {y+=speed;this.direct = 1;}
//						else continue;
                            else break;//这样才不会有敌人坦克凑在你附近不动
//						else this.direct = (int)(Math.random()*4);
                            if (min % 27 == 0)
                                this.shotEnemy();
                            try {
                                Thread.sleep(50);
                            } catch (Exception e) {
                                log.error("", e);
                            }
                        }

                    }

                    break;
                case 1:
                    for (int i = 0; i < 30; i++) {
                        min++;
//					if(!bri)this.direct = (int)(Math.random()*4);

                        if (y < 530) {
                            if (TankTool.ablePass(this, hero) && overlap && bri) y += speed;
//						    if(bri) y+=speed;
//						    else {y-=speed;this.direct = 0;}
//						else continue;
                            else break;
                            if (min % 27 == 0) {
                                this.shotEnemy();
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
                            if (TankTool.ablePass(this, hero) && overlap && bri) x -= speed;
//						    if(bri)x-=speed;
//						    else{x+=speed;this.direct = 3;}
//						else continue;
                            else break;
                            if (min % 27 == 0)
                                this.shotEnemy();
                            try {
                                Thread.sleep(50);
                            } catch (Exception e) {
                                log.error("", e);
                            }
                        }

                    }

                    break;
                case 3:
                    for (int i = 0; i < 30; i++) {
                        min++;
//					if(!bri)this.direct = (int)(Math.random()*4);

                        if (x < 710) {
                            if (TankTool.ablePass(this, hero) && overlap && bri) {
                                x += speed;
                            }
//						     if(bri)x+=speed;
//						     else{x-=speed;this.direct = 2;}
                            else break;
//						else continue;
                            if (min % 27 == 0)
                                this.shotEnemy();
                            try {
                                Thread.sleep(50);
                            } catch (Exception e) {
                                log.error("", e);
                            }
                        }


                    }
                    break;
                default:
                    break;
            }

            //判断坦克是否死亡
            if (!this.isAlive()) {
                //让坦克退出while即退出线程
                hero.addPrize(1);

                break;
            }
        }
    }

}
