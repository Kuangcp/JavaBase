/**
 *   绘制开始游戏的界面
 * 
 * 如果要实现闪烁效果 就实现接口，定义一个变量，run里面++，
 *   设置变量满足什么条件再画出图片
 * 
 */
package com.github.kuangcp.tank.v3;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MyPanel303 extends JPanel {

	Image First;
	
	public MyPanel303(){
		try {
			First = ImageIO.read(getClass().getResource("/images/Tank.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void paint(Graphics g){
		g.drawImage(First, 0, 0, 760,560,this);
		
		//直接设置字符串，不用图片
		Font my = new Font("微软雅黑", Font.BOLD,15);
		g.setFont(my);
//		String s = "击中40辆坦克可以获胜";
		String s = "永无终止";
		g.setColor(Color.lightGray);
		g.drawString(s, 50, 500);
		

	}

}
