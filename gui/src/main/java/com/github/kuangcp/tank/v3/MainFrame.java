package com.github.kuangcp.tank.v3;

import com.github.kuangcp.tank.constant.StageCommand;
import com.github.kuangcp.tank.panel.TankGroundPanel;
import com.github.kuangcp.tank.panel.HeroInfoPanel;
import com.github.kuangcp.tank.panel.StageActionPanel;
import com.github.kuangcp.tank.panel.StarterPanel;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * 坦克3.0 版本：
 * <p>
 * 1 多颗子弹连发  （限制：最多五颗子弹即五个线程）
 * 2 敌人的坦克也能够发射子弹
 * 3 打到敌人坦克敌人消失 做出爆炸效果
 * 4 让敌方坦克可以自由的随机上下左右移动
 * 5 控制我方坦克和敌方坦克在规定范围内移动
 * 6 有窗口提示信息
 * 7 坦克不重叠，与障碍物不重叠
 * <p>
 * <p>
 * 8 可以把这里的窗体做成线程 但是都是静态成员，所以就算做成线程 出来的新界面也还是原来的属性（一样的界面）
 * 总结：不应该随便设置静态成员的  尤其是可能会新建的成员
 * 解决方案：
 * 在需要用到无法引用到的对象时，除了做成静态，还可以在当前添加这个对象的引用
 * 然后构造器 里传入所要调用的对象 就能解决问题了，多线程启动也会是初始化的状态
 * ！！最好放在别的函数里，不要改动构造器，会对以前的代码有影响（）
 * 9 可以暂停游戏，继续游戏
 * 原来这么简单，设置动的那些Speed为0 不就是静止么。。还有方向不能动 还有焦点的自动跳转
 * 10 可以继续上局游戏开始玩
 * 11 有游戏的音效（操纵声音文件）
 */
@Slf4j
@SuppressWarnings("serial")
public class MainFrame extends JFrame implements Runnable {

    public volatile TankGroundPanel groundPanel = null;//坦克的主画板
    public StageActionPanel mp2 = null;//放按钮的画板
    public StageActionPanel mpe1 = null;//对按钮事件监听处理的
    public HeroInfoPanel mp3 = null;  //显示属性的画板
    public StarterPanel Fir;
    public JButton startBtn = null, jb2 = null, jb3, jb4;  //按钮
    public JSplitPane jsp1, jsp2;//拆分窗格
    public JLabel jl1 = null, jl2 = null, jl3 = null, me = null, prizeNo = null;
    public boolean firstStart = true;  //判断是否首次运行

    //作出我需要的菜单
    JMenuBar jmb = null;
    //开始游戏
    JMenu jm1 = null;
    JMenu jm2 = null;
    JMenuItem jmil = null;
    //退出系统
    JMenuItem jmi2 = null;
    //存盘退出
    JMenuItem jmi3 = null;
    JMenuItem jmi4 = null;
    //帮助窗口
    JMenuItem Help = null;

    public void run() {
        final long start = System.currentTimeMillis();
        if (Objects.nonNull(groundPanel)) {
            groundPanel.exit();
        }
        if (Objects.nonNull(mp3)) {
            mp3.exit();
        }

        groundPanel = new TankGroundPanel();
        mpe1 = new StageActionPanel(this, groundPanel.hero, groundPanel.enemyList, groundPanel.bricks, groundPanel.irons, TankGroundPanel.enemyTankMap, TankGroundPanel.myself);//监听按钮事件的画布对象
        mp2 = new StageActionPanel(this, groundPanel.hero, groundPanel.enemyList, groundPanel.bricks, groundPanel.irons, TankGroundPanel.enemyTankMap, TankGroundPanel.myself);//放按钮的画布

        //提示信息
        jl1 = new JLabel("           :                    : " + groundPanel.enemyList.size());
        prizeNo = new JLabel("已击杀    ：");//战绩的标签
        me = new JLabel("Myth");

        mp3 = new HeroInfoPanel(jl1, groundPanel.enemyList, prizeNo);//显示一些属性

        if (!firstStart) {
            //已经成为一个线程 要启动它
            Thread t = new Thread(groundPanel);
            t.setName("windowPanel");
            t.start();
            Thread t2 = new Thread(mp3);
            t2.setName("mp3");
            t2.start();
        }

        //创建菜单及菜单选项
        jmb = new JMenuBar();
        jm1 = new JMenu("Game(G)");
        jm2 = new JMenu("Help(H)");

        //设置快捷方式
        jm1.setMnemonic('G');
        jm2.setMnemonic('H');
        jmil = new JMenuItem("New Game(N)");
        jmi2 = new JMenuItem("Pause");
        jmi3 = new JMenuItem("Save & Exit(C)");
        jmi4 = new JMenuItem("ContinueLast(S)");
        Help = new JMenuItem("Setting");

        //
        Help.addActionListener(mpe1);
        Help.setActionCommand("Help");/////////
        //注册监听
        jmi4.addActionListener(mpe1);
        jmi4.setActionCommand("Continue");////////////
        //注册监听
        jmi3.addActionListener(mpe1);
        jmi3.setActionCommand("saveExit");////////
        //
        jmi2.addActionListener(mpe1);
        jmi2.setActionCommand("暂停");/////////
        jmi2.setMnemonic('E');
        //对jmil相应
        jmil.addActionListener(mpe1);
        jmil.setActionCommand(StageCommand.START);//////

        jm1.add(jmil);
//				jm1.add(jmi2);
        jm1.add(jmi3);
        jm1.add(jmi4);

        jm2.add(Help);

        jmb.add(jm1);
        jmb.add(jm2);

        jb3 = new JButton("暂停游戏");
        jb3.addActionListener(mpe1); //注册监听
        jb3.setActionCommand("暂停");
        jb4 = new JButton("继续游戏");
        jb4.addActionListener(mpe1); //注册监听
        jb4.setActionCommand("继续");

        jb2 = new JButton("退出游戏");
        jb2.addActionListener(mpe1); //注册监听
        jb2.setActionCommand("结束");

        startBtn = new JButton(firstStart ? "游戏开始" : "重新开始");
        startBtn.addActionListener(mpe1);
        startBtn.setActionCommand(StageCommand.START);

        mp2.add(startBtn);
        mp2.add(jb2);
        mp2.add(jb3);
        mp2.add(jb4);

        //显示属性的窗体：
        mp3.setLayout(new GridLayout(6, 1, 0, 0));

        mp3.add(jl1);//网格布局
        mp3.add(prizeNo);
//		mp3.add(jl2);
//		mp3.add(jl3);
        mp3.add(me);
//		jl1 =new JLabel("898908098");//不会对上面造成任何影响

        jsp2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mp2, mp3);//水平
        jsp2.setDividerLocation(150);
        Fir = new StarterPanel();
        if (!firstStart) jsp1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, groundPanel, jsp2);//垂直
        else jsp1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, Fir, jsp2);
        jsp1.setDividerLocation(760);

        //把画板加入JFrame
        this.add(jsp1, BorderLayout.CENTER);
//		this.add(mp2,BorderLayout.EAST);
//		this.add(mp,BorderLayout.CENTER);

        //注册键盘监听
        //下面的语句翻译为 ：当前类的监听者是mp
        this.addKeyListener(groundPanel);
        this.setJMenuBar(jmb);
        //焦点跳转  tab切换
        this.setFocusable(getFocusTraversalKeysEnabled());

        //JFrame 窗体的属性
        this.setTitle("Tank");
        this.setLocation(150, 60);
        this.setSize(1000, 625);

        final long beforeVisible = System.currentTimeMillis();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final long now = System.currentTimeMillis();
        log.info("total:{} visible:{}", (now - start), (now - beforeVisible));
    }
}