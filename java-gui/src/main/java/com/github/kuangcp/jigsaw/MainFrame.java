package com.github.kuangcp.jigsaw;

import java.awt.BorderLayout;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 18-9-16-上午10:22
 */
public class MainFrame extends JFrame {

  static Vector<Part> partVector = new Vector<>();

  public static void main(String[] args) {
    new MainFrame();
  }

  private MainFrame() {
    for (int i = 0; i < Part.MAX * Part.MAX; i++) {
      Part p = new Part(i / Part.MAX, i % Part.MAX, i);
      partVector.add(p);
    }

    com.github.kuangcp.jigsaw.Button button = new Button();
    //	Vector <JLabel>tu;  集合更好些 只是不太会用
    JButton jb1 = new JButton("开始游戏");
    jb1.addActionListener(button);//注册监听
    jb1.setActionCommand("开始");//指定特定的命令

    Panelp jp2 = new Panelp();
    jp2.add(jb1, BorderLayout.SOUTH);

    Panelp jp1 = new Panelp();
    jp1.setSize(800, 600);
    //设置监听 不过为什么是要写当前对象，而不是写jp画板呢
    //事件源是JFrame 事件监听者是jp1
    this.addKeyListener(jp1);

    this.add(jp1, BorderLayout.CENTER);
//		this.add(jp2,BorderLayout.SOUTH);
    //加上按钮的监听后，键盘监听就失效了。？？？
//		jp.add(jb1,BorderLayout.SOUTH);
    this.setSize(597, 615);
//    this.setSize(750, 730);
    this.setLocation(600, 0);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);

//    disrupt();
  }

  /**
   * 打乱顺序
   */
  private void disrupt() {
    for (int i = 0; i < 80; i++) {
      Move k = new Move();
      int dir;
      dir = (int) (Math.random() * 5);
      switch (dir) {
        case 1:
          k.moveA();
          break;
        case 2:
          k.moveD();
          break;
        case 3:
          k.moveS();
          break;
        case 4:
          k.moveW();
          break;
        default:
          break;
      }
    }
  }
}

/*失败的尝试
int x0,y0,position;
public void moveW(){
	x0 = -1;y0 = -1;position = 0;
	for (int i=0;i<game.partVector.size();i++){
		try {
			if (game.partVector.get(i).l.getText().equals(" ")){
				x0=game.partVector.get(i).x;
				y0=game.partVector.get(i).y;
				position=game.partVector.get(i).position;
			}
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("没找到");
			continue;
		}
		System.out.println("X="+x0+" Y="+y0);
	}
	for (int i=0;i<game.partVector.size();i++){
		if ((game.partVector.get(i).y-1) == y0){
			temp = game.partVector.get(i);
			game.partVector.set(i, game.partVector.get(position));
			game.partVector.set(position, temp);

		}
	}
	for(int i=0;i<9;i++){
		game.jp.add(game.partVector.get(8-i).l);
	}
}

		//设置网格布局
/*		jp.setLayout(new GridLayout(3,3));

		//加入图片
		partVector.get(0).l = new JLabel(new ImageIcon("images/h/11_0.jpg"));
		partVector.get(1).l = new JLabel(new ImageIcon("images/h/11_1.jpg"));
		partVector.get(2).l = new JLabel(" "); //设置空白格  不能直接就赋值一个null 会报错空指针异常
//		partVector.get(2).l = new JLabel(new ImageIcon("images/h/11_2.jpg"));
		partVector.get(3).l = new JLabel(new ImageIcon("images/h/11_3.jpg"));
		partVector.get(4).l = new JLabel(new ImageIcon("images/h/11_4.jpg"));
		partVector.get(5).l = new JLabel(new ImageIcon("images/h/11_5.jpg"));
		partVector.get(6).l = new JLabel(new ImageIcon("images/h/11_6.jpg"));
		partVector.get(7).l = new JLabel(new ImageIcon("images/h/11_7.jpg"));
		partVector.get(8).l = new JLabel(new ImageIcon("images/h/11_8.jpg"));

		for(int i=0;i<9;i++){
			jp.add(partVector.get(i).l);
		}
		*/
//		if (partVector.get(2).l.getText().equals(" ")) System.out.println(" 空白格可以判断");//如果是空白格就能运行，否者就会抛空指针异常



