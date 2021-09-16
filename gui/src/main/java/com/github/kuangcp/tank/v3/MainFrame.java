package com.github.kuangcp.tank.v3;

import com.github.kuangcp.tank.constant.ButtonCommand;
import com.github.kuangcp.tank.panel.HeroInfoPanel;
import com.github.kuangcp.tank.panel.StageActionPanel;
import com.github.kuangcp.tank.panel.StarterPanel;
import com.github.kuangcp.tank.panel.TankGroundPanel;
import com.github.kuangcp.tank.panel.event.HeroInfoPanelRefreshEvent;
import com.github.kuangcp.tank.resource.AvatarImgMgr;
import com.github.kuangcp.tank.resource.ResourceMgr;
import com.github.kuangcp.tank.util.executor.MonitorExecutor;
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
    public StageActionPanel actionPanel = null;//放按钮的画板

    public HeroInfoPanel heroInfoPanel = null;  //显示属性的画板
    private HeroInfoPanelRefreshEvent loopEvent = null;


    public StarterPanel starterPanel;
    public JButton startBtn = null, jb2 = null, jb3, jb4;  //按钮
    public JSplitPane centerPanel, rightAreaPanel;//拆分窗格
    public JLabel tankCounter = null, jl2 = null, jl3 = null, me = null, prizeNo = null;
    public boolean firstStart = true;  //判断是否首次运行, 通过开始按钮触发

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
    JMenuItem setting = null;

    public MainFrame() {
        ResourceMgr.loadResource();
    }

    public void run() {
        final long start = System.currentTimeMillis();

        if (Objects.nonNull(groundPanel)) {
            groundPanel.exit();
        }
        // FIXME 复用对象导致性能缓慢
//        else {
//            groundPanel = new TankGroundPanel();
//        }

        groundPanel = new TankGroundPanel();

        if (Objects.nonNull(heroInfoPanel)) {
            loopEvent.stop();
            heroInfoPanel.setEts(groundPanel.enemyList);
        } else {
            //提示信息
            tankCounter = new JLabel("           :                    : " + groundPanel.enemyList.size());
            prizeNo = new JLabel("已击杀    ：");//战绩的标签
            me = new JLabel("Myth");

            heroInfoPanel = new HeroInfoPanel(tankCounter, groundPanel.enemyList, prizeNo);
        }

        actionPanel = new StageActionPanel(this, groundPanel.hero, groundPanel.enemyList, groundPanel.bricks,
                groundPanel.irons, TankGroundPanel.enemyTankMap, TankGroundPanel.myself);//放按钮的画布

        loopEvent = new HeroInfoPanelRefreshEvent(heroInfoPanel);
        heroInfoPanel.setRefreshEvent(loopEvent);
        MonitorExecutor.addLoopEvent(loopEvent);

        if (!firstStart) {
            Thread t = new Thread(groundPanel);
            t.setName("tankGroundPanel");
            t.start();
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
        setting = new JMenuItem("Setting");

        //
        setting.addActionListener(actionPanel);
        setting.setActionCommand(ButtonCommand.SETTING_FRAME);
        //注册监听
        jmi4.addActionListener(actionPanel);
        jmi4.setActionCommand("Continue");
        //注册监听
        jmi3.addActionListener(actionPanel);
        jmi3.setActionCommand("saveExit");
        //
        jmi2.addActionListener(actionPanel);
        jmi2.setActionCommand("暂停");
        jmi2.setMnemonic('E');
        //对jmil相应
        jmil.addActionListener(actionPanel);
        jmil.setActionCommand(ButtonCommand.START);

        jm1.add(jmil);
//				jm1.add(jmi2);
        jm1.add(jmi3);
        jm1.add(jmi4);

        jm2.add(setting);

        jmb.add(jm1);
        jmb.add(jm2);

        jb3 = new JButton("暂停游戏");
        jb3.addActionListener(actionPanel); //注册监听
        jb3.setActionCommand(ButtonCommand.PAUSE);

        jb4 = new JButton("继续游戏");
        jb4.addActionListener(actionPanel); //注册监听
        jb4.setActionCommand(ButtonCommand.RESUME);

        jb2 = new JButton("退出游戏");
        jb2.addActionListener(actionPanel); //注册监听
        jb2.setActionCommand(ButtonCommand.EXIT);

        startBtn = new JButton(firstStart ? "游戏开始" : "重新开始");
        startBtn.addActionListener(actionPanel);
        startBtn.setActionCommand(ButtonCommand.START);

        actionPanel.add(startBtn);
        actionPanel.add(jb2);
        actionPanel.add(jb3);
        actionPanel.add(jb4);

        //显示属性的窗体：
        heroInfoPanel.setLayout(new GridLayout(6, 1, 0, 0));

        heroInfoPanel.add(tankCounter);//网格布局
        heroInfoPanel.add(prizeNo);
//		mp3.add(jl2);
//		mp3.add(jl3);
        heroInfoPanel.add(me);
//		jl1 =new JLabel("898908098");//不会对上面造成任何影响

        rightAreaPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, actionPanel, heroInfoPanel);//水平
        rightAreaPanel.setDividerLocation(150);

        starterPanel = new StarterPanel();
        if (!firstStart) {
            centerPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, groundPanel, rightAreaPanel);
        } else {
            centerPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, starterPanel, rightAreaPanel);
        }
        centerPanel.setDividerLocation(760);
        this.add(centerPanel, BorderLayout.CENTER);

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
        this.setIconImage(AvatarImgMgr.instance.curImg);

        final long beforeVisible = System.currentTimeMillis();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final long now = System.currentTimeMillis();
        log.info("[init] mainFrame. total:{} visible:{}", (now - start), (now - beforeVisible));
    }
}