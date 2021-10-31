package com.github.kuangcp.tank.domain;

/**
 * 砖块
 */
public class Brick extends Hinder {

    public Brick(int hx, int hy) {
        super(hx, hy);
    }

    @Override
    public int getWidth() {
        return 5;
    }

    @Override
    public int getHeight() {
        return 5;
    }

    /**
     * 构造器全是沿用父类的，父类要是没有无参构造器，子类有了就属于非法
     * 并且 父类有了含参构造器，子类必须也要有，显式的super一下
     */
    public Brick() {
    }
    //被舍弃  不使用
    /*画出砖块  20*10大小 为了便于使用 就直接(x,y)到(x,y)画出矩形  虽然实现了但是不利于控制死亡*/
	/*public  void d(Graphics g,Vector<Brick> bricks,int startX,int startY,int endX,int endY){

//		g.setColor(Color.LIGHT_GRAY);//钢板的颜色
		g.setColor(Color.orange);//砖块颜色
		
		for( hx=startX;hx<endX;hx+=20){
			for (hy=startY;hy<endY;hy+=10){
				Brick bs = new Brick(hx,hy);
				g.fill3DRect(bs.hx, bs.hy, 20, 10, false);
				bricks.add(bs);
				
			}
		}
		
	}*/


}
