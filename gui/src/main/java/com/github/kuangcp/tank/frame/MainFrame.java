package com.github.kuangcp.tank.frame;

import com.github.kuangcp.tank.mgr.RoundMapMgr;
import com.github.kuangcp.tank.panel.StageActionPanel;
import com.github.kuangcp.tank.panel.StarterPanel;
import com.github.kuangcp.tank.panel.TankGroundPanel;
import com.github.kuangcp.tank.resource.AvatarImgMgr;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * 坦克3.0 版本：
 * <p>
 * 1 多颗子弹连发  （限制：最多五颗子弹即五个线程）
 * 2 敌人的坦克也能够发射子弹
 * 3 打到敌人坦克敌人消失 做出爆炸效果
 * 4 让敌方坦克可以自由的随机上下左右移动
 * 5 控制我方坦克和敌方坦克在规定范围内移动
 * 6 有相关提示信息刷新
 * 7 坦克不重叠，与障碍物不重叠
 * 8 游戏结束机制
 * 9 可以暂停游戏，继续游戏
 * 10 可以继续上局游戏开始玩
 * 11 有游戏的音效（操纵声音文件）
 */
@Slf4j
public class MainFrame extends JFrame implements Runnable {

    public volatile TankGroundPanel groundPanel;//主界面
    public static StageActionPanel actionPanel = null;//放按钮的画板

    public StarterPanel starterPanel;
    public boolean firstStart = true;  //判断是否首次运行, 通过开始按钮触发

    public MainFrame() {
        starterPanel = new StarterPanel();
        groundPanel = new TankGroundPanel();
        actionPanel = new StageActionPanel(this);

        Thread t = new Thread(groundPanel);
        t.setName("tankGroundPanel");
        t.start();

        this.setTitle("Tank");
        this.setLocation(680, 290);
        this.setSize(RoundMapMgr.instance.border.getTotalX(), RoundMapMgr.instance.border.getTotalY());

        this.setUndecorated(true);
    }

    public void run() {
        final long start = System.currentTimeMillis();

        // FIXME 复用对象导致性能缓慢
//        else {
//            groundPanel = new TankGroundPanel();
//        }

        if (!firstStart) {
            this.add(groundPanel, BorderLayout.CENTER);
        } else {
            this.add(starterPanel, BorderLayout.CENTER);
        }

        this.supportMouseMoveWindow();
        this.addKeyListener(groundPanel);

        // 焦点跳转  tab切换
        this.setFocusable(getFocusTraversalKeysEnabled());

        //JFrame 窗体的属性
        this.setIconImage(AvatarImgMgr.instance.curImg);

        final long beforeVisible = System.currentTimeMillis();
        this.setVisible(true);
        final long now = System.currentTimeMillis();
        log.info("[init] mainFrame. total:{} visible:{}", (now - start), (now - beforeVisible));
    }

    private void supportMouseMoveWindow() {
        class DeltaLocation {
            int xOld = 0;
            int yOld = 0;
        }

        final DeltaLocation deltaLocation = new DeltaLocation();
        //处理拖动事件
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                deltaLocation.xOld = e.getX();
                deltaLocation.yOld = e.getY();
            }
        });
        final MainFrame mainFrame = this;
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int xOnScreen = e.getXOnScreen();
                int yOnScreen = e.getYOnScreen();
                int xx = xOnScreen - deltaLocation.xOld;
                int yy = yOnScreen - deltaLocation.yOld;
                mainFrame.setLocation(xx, yy);
            }
        });
    }
}