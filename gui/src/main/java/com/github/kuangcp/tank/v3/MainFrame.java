package com.github.kuangcp.tank.v3;

import com.github.kuangcp.tank.constant.ButtonCommand;
import com.github.kuangcp.tank.panel.HeroInfoPanel;
import com.github.kuangcp.tank.panel.StageActionPanel;
import com.github.kuangcp.tank.panel.StarterPanel;
import com.github.kuangcp.tank.panel.TankGroundPanel;
import com.github.kuangcp.tank.resource.AvatarImgMgr;
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
    public static StageActionPanel actionPanel = null;//放按钮的画板

    public HeroInfoPanel heroInfoPanel = null;  //显示属性的画板
//    private HeroInfoPanelRefreshEvent loopEvent = null;

    public StarterPanel starterPanel;
    public JLabel tankCounter = null, me = null, prizeNo = null;
    public boolean firstStart = true;  //判断是否首次运行, 通过开始按钮触发

    //作出我需要的菜单
    JMenuBar menuBar = null;
    //开始游戏
    JMenu gameMenu = null;
    JMenu helpMenu = null;
    JMenuItem jmil = null;
    //退出系统
    JMenuItem jmi2 = null;
    //存盘退出
    JMenuItem jmi3 = null;
    JMenuItem jmi4 = null;
    //帮助窗口
    JMenuItem setting = null;

    public MainFrame() {
        groundPanel = new TankGroundPanel();
        actionPanel = new StageActionPanel(this);

        Thread t = new Thread(groundPanel);
        t.setName("tankGroundPanel");
        t.start();

        this.setTitle("Tank");
        this.setLocation(680, 290);
        this.setSize(760, 590);

//        this.setUndecorated(true);
    }

    public void run() {
        final long start = System.currentTimeMillis();

        // FIXME 复用对象导致性能缓慢
//        else {
//            groundPanel = new TankGroundPanel();
//        }

        if (Objects.nonNull(heroInfoPanel)) {
//            loopEvent.stop();
            heroInfoPanel.setEts(groundPanel.enemyList);
        } else {
            tankCounter = new JLabel("");
            prizeNo = new JLabel("");
            me = new JLabel("Myth");

            heroInfoPanel = new HeroInfoPanel(tankCounter, groundPanel.enemyList, prizeNo);
        }

//        loopEvent = new HeroInfoPanelRefreshEvent(heroInfoPanel);
//        heroInfoPanel.setRefreshEvent(loopEvent);
//        MonitorExecutor.addLoopEvent(loopEvent);


        //创建菜单及菜单选项
        menuBar = new JMenuBar();
        gameMenu = new JMenu("Game(G)");
        helpMenu = new JMenu("Help(H)");
        //设置快捷方式
        gameMenu.setMnemonic('G');
        helpMenu.setMnemonic('H');

        jmil = new JMenuItem("New Game(N)");
        jmi2 = new JMenuItem("Pause");
        jmi3 = new JMenuItem("Save & Exit(C)");
        jmi4 = new JMenuItem("ContinueLast(S)");

        setting = new JMenuItem("Setting");
        setting.addActionListener(SettingFrame.instance);
        setting.setActionCommand(ButtonCommand.SETTING_FRAME);
        helpMenu.add(setting);

        jmi4.addActionListener(actionPanel);
        jmi4.setActionCommand("Continue");

        jmi3.addActionListener(actionPanel);
        jmi3.setActionCommand("saveExit");

        jmi2.addActionListener(actionPanel);
        jmi2.setActionCommand("暂停");
        jmi2.setMnemonic('E');

        jmil.addActionListener(actionPanel);
        jmil.setActionCommand(ButtonCommand.START);

        gameMenu.add(jmil);

        final JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(actionPanel);
        exitItem.setActionCommand(ButtonCommand.EXIT);
        gameMenu.add(exitItem);

        gameMenu.add(jmi3);
        gameMenu.add(jmi4);

        menuBar.add(gameMenu);
        menuBar.add(helpMenu);

//        jb3 = new JButton("暂停游戏");
//        jb3.addActionListener(actionPanel); //注册监听
//        jb3.setActionCommand(ButtonCommand.PAUSE);
//
//        jb4 = new JButton("继续游戏");
//        jb4.addActionListener(actionPanel); //注册监听
//        jb4.setActionCommand(ButtonCommand.RESUME);
//
//        jb2 = new JButton("退出游戏");
//        jb2.addActionListener(actionPanel); //注册监听
//        jb2.setActionCommand(ButtonCommand.EXIT);
//
//        startBtn = new JButton(firstStart ? "游戏开始" : "重新开始");
//        startBtn.addActionListener(actionPanel);
//        startBtn.setActionCommand(ButtonCommand.START);
//
//        actionPanel.add(startBtn);
//        actionPanel.add(jb2);
//        actionPanel.add(jb3);
//        actionPanel.add(jb4);

        //显示属性的窗体：
        heroInfoPanel.setLayout(new GridLayout(3, 1, 0, 0));

        heroInfoPanel.add(tankCounter);//网格布局
        heroInfoPanel.add(prizeNo);
        heroInfoPanel.add(me);

//        rightAreaPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, actionPanel, heroInfoPanel);//水平
//        rightAreaPanel.setDividerLocation(150);

        starterPanel = new StarterPanel();
        if (!firstStart) {
            this.add(groundPanel, BorderLayout.CENTER);
//            centerPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, groundPanel, rightAreaPanel);
        } else {
//            centerPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, starterPanel, rightAreaPanel);
            this.add(starterPanel, BorderLayout.CENTER);
        }
//        centerPanel.setDividerLocation(760);
//        this.add(centerPanel, BorderLayout.CENTER);

        this.addKeyListener(groundPanel);

//        this.setJMenuBar(menuBar);
        // 焦点跳转  tab切换
        this.setFocusable(getFocusTraversalKeysEnabled());

        //JFrame 窗体的属性
        this.setIconImage(AvatarImgMgr.instance.curImg);

        final long beforeVisible = System.currentTimeMillis();
        this.setVisible(true);
        final long now = System.currentTimeMillis();
        log.info("[init] mainFrame. total:{} visible:{}", (now - start), (now - beforeVisible));
    }
}