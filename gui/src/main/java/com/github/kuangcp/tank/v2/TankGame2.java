package com.github.kuangcp.tank.v2;
/**
 * 能控制坦克运动，有墙的机制
 * <p>
 * <p>
 * 坦克2.0 版本 目标：按下j键发射直线的子弹
 * 子弹是一个对象，线程
 */

import javax.swing.*;

@SuppressWarnings("serial")
public class TankGame2 extends JFrame {


    public static MyPanel mp = null;//画板


    //	JS j = null;
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        TankGame2 Tank = new TankGame2();
    }

    //最外层JFrame的构造函数
    public TankGame2() {

        mp = new MyPanel();//已经成为一个线程 要启动它
        Thread t = new Thread(mp);
        t.start();

//		j = new JS();   

//		this.add(j);
        this.add(mp);

        //注册键盘监听
        //下面的语句翻译为 ：当前类的监听者是mp
        this.addKeyListener(mp);
//		this.addKeyListener(j);

        this.setLocation(900, 200);
        this.setSize(500, 400);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}


/////////////////////////////////////////////////////////////////////////////

/*
//为了能一边走一边发子弹而设计的专门处理按下j键子弹发射的类
class JS extends JPanel implements KeyListener{

	Hero hero=null;
	public JS(){}
	public JS(Hero h){
		hero = h;
	}
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyChar() == KeyEvent.VK_J){
			//开火
			hero.shotEnemy();
		}
		
		//必须重新绘制窗口，不然上面的方法不能视觉上动起来
		this.repaint();
	
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
*/
