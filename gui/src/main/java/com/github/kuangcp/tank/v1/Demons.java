
package com.github.kuangcp.tank.v1;


import com.github.kuangcp.tank.util.TankTool;
import com.github.kuangcp.tank.v2.Shot;
import lombok.extern.slf4j.Slf4j;

import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 敌方坦克父类
 * 可以继续延伸做出多样化的坦克
 * <p>
 * FIXME 子弹集合是敌方坦克的成员属性 坦克内存一回收 子弹也就没了， 坦克死了 子弹也应该继续飞
 */
@Slf4j
public class Demons extends Tank implements Runnable {

    public boolean alive;
    public Vector<Shot> ds = new Vector<>();//子弹集合
    private final ExecutorService shotExecutePool;
    public Vector<Demons> ets;
    public Vector<Brick> bricks;
    public Vector<Iron> irons;
    public Shot s = null;
    public int maxLiveShot = 16; //子弹线程存活的最大数
    Hero hero;

    boolean with = true; //同类之间有无重叠（true是可以往前走）
    boolean bri = true;
    boolean to = true;

    //继承了属性，即使直接使用父类的构造器，构造器也一定要显式声明
    public Demons(int x, int y, int speed, int direct) {
        super(x, y, speed);
        type = 1;
        this.direct = direct;
        this.speed = speed;
        this.alive = true;
        this.shotExecutePool = Executors.newFixedThreadPool(this.maxLiveShot);
    }

    public void SetInfo(Hero hero, Vector<Demons> ets, Vector<Brick> bricks, Vector<Iron> irons) {
        this.hero = hero;
        this.ets = ets;
        this.bricks = bricks;
        this.irons = irons;
    }

    public boolean isWith() {
        return with;
    }

    public void setWith(boolean with) {
        this.with = with;
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
     * 重新封装
     */

    /**
     * 发射子弹    函数
     */
    public void shotEnemy() {
        //判断坦克方向来 初始化子弹的起始发射位置
        if (!this.alive || ds.size() >= this.maxLiveShot) {
            return;
        }
        switch (this.getDirect()) {
            case 0: {//0123 代表 上下左右
                s = new Shot(this.getX() - 1, this.getY() - 15, 0);
                ds.add(s);
                break;
            }
            case 1: {
                s = new Shot(this.getX() - 2, this.getY() + 15, 1);
                ds.add(s);
                break;
            }
            case 2: {
                s = new Shot(this.getX() - 15 - 2, this.getY(), 2);
                ds.add(s);
                break;
            }
            case 3: {
                s = new Shot(this.getX() + 15 - 2, this.getY() - 1, 3);
                ds.add(s);
                break;
            }
        }
        //启动子弹线程
        shotExecutePool.execute(s);
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
                    Demons et = ets.get(i);
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
                    Demons et = ets.get(i);
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
                    Demons et = ets.get(i);
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
                    Demons et = ets.get(i);
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
            to = true;
            for (Demons et : ets) {
                if (!TankTool.Rush(this, et)) {
                    to = (false);
                    break;
                }
            }
            for (Brick brick : bricks) {
                if (TankTool.hasHint(this, brick))
                    to = false;
            }
            for (Iron iron : irons) {
                if (TankTool.hasHint(this, iron))
                    to = false;
            }
            if (to && TankTool.Rush(this, hero)) {
                y -= speed;
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return false;
            }

        } else return false;
        return true;
    }

    public boolean toDown() {
        if (y < 530) {
            to = true;
            for (Demons et : ets) {
                if (!TankTool.Rush(this, et)) {
                    to = (false);
                    break;
                }
            }
            for (Brick brick : bricks) {
                if (TankTool.hasHint(this, brick))
                    to = false;
            }
            for (Iron iron : irons) {
                if (TankTool.hasHint(this, iron))
                    to = false;
            }
            if (to && TankTool.Rush(this, hero)) {
                y += speed;
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
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
            to = true;
            for (Demons et : ets) {
                if (!TankTool.Rush(this, et)) {
                    to = false;
                    break;
                }
            }
            for (Brick brick : bricks) {
                if (TankTool.hasHint(this, brick))
                    to = false;
            }
            for (Iron iron : irons) {
                if (TankTool.hasHint(this, iron))
                    to = false;
            }
            if (to && TankTool.Rush(this, hero)) {
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
            to = true;
            for (Demons et : ets) {
                if (!TankTool.Rush(this, et)) {
                    to = (false);
                    break;
                }
            }
            for (Brick brick : bricks) {
                if (TankTool.hasHint(this, brick))
                    to = false;
            }
            for (Iron iron : irons) {
                if (TankTool.hasHint(this, iron))
                    to = false;
            }
            if (to && TankTool.Rush(this, hero)) {
                x += speed;
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
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

    public void run() {
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
                            if (TankTool.Rush(this, hero) && with) y -= speed;
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
                                e.printStackTrace();
                            }
                        }

                    }

                    break;
                case 1:
                    for (int i = 0; i < 30; i++) {
                        min++;
//					if(!bri)this.direct = (int)(Math.random()*4);

                        if (y < 530) {
                            if (TankTool.Rush(this, hero) && with && bri) y += speed;
//						    if(bri) y+=speed;
//						    else {y-=speed;this.direct = 0;}
//						else continue;
                            else break;
                            if (min % 27 == 0)
                                this.shotEnemy();
                            try {
                                Thread.sleep(50);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    break;
                case 2:
                    for (int i = 0; i < 30; i++) {
                        min++;
//					if(!bri)this.direct = (int)(Math.random()*4);

                        if (x > 30) {
                            if (TankTool.Rush(this, hero) && with && bri) x -= speed;
//						    if(bri)x-=speed;
//						    else{x+=speed;this.direct = 3;}
//						else continue;
                            else break;
                            if (min % 27 == 0)
                                this.shotEnemy();
                            try {
                                Thread.sleep(50);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    break;
                case 3:
                    for (int i = 0; i < 30; i++) {
                        min++;
//					if(!bri)this.direct = (int)(Math.random()*4);

                        if (x < 710) {
                            if (TankTool.Rush(this, hero) && with && bri) x += speed;
//						     if(bri)x+=speed;
//						     else{x-=speed;this.direct = 2;}
                            else break;
//						else continue;
                            if (min % 27 == 0)
                                this.shotEnemy();
                            try {
                                Thread.sleep(50);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    }
                    break;
                default:
                    break;
            }

            //判断坦克是否死亡
            if (!this.getisLive()) {
                //让坦克退出while即退出线程
                hero.setPrize(hero.getPrize() + 1);
                break;
            }
        }
    }

}
