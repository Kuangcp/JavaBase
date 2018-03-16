package com.github.kuangcp.sort;

import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 * @date 18-3-16  下午10:29
 */
public class GetRunTimeTest {
    GetRunTime getRunTime = new GetRunTime();

    @Test
    public void testStart() throws Exception {
        getRunTime.Start();
    }

    @Test
    public void testEnd() throws Exception {
        getRunTime.End("s");
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme