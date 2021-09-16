package com.github.kuangcp.tank.panel.event;

import com.github.kuangcp.tank.panel.HeroInfoPanel;
import com.github.kuangcp.tank.util.executor.AbstractLoopEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author https://github.com/kuangcp on 2021-09-17 01:58
 */
@Slf4j
public class HeroInfoPanelRefreshEvent extends AbstractLoopEvent {

    public static final long fixedDelayTime = 600;

    private final HeroInfoPanel heroInfoPanel;

    public HeroInfoPanelRefreshEvent(HeroInfoPanel heroInfoPanel) {
        this.heroInfoPanel = heroInfoPanel;

        this.setFixedDelayTime(fixedDelayTime);
    }

    @Override
    public void run() {
        heroInfoPanel.refreshData();
    }
}
