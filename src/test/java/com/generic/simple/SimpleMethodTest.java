package com.generic.simple;

import org.apache.poi.ss.formula.functions.T;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp on 18-1-11  下午5:45
 *
 * @author kuangcp
 */
public class SimpleMethodTest {

    /**
     * 和核心技术卷上不一样,应该是版本的问题, 入参就已经限制成数组了,怎么可能会有别的类型混进来
     * @throws Exception
     */
    @Test
    public void testGetMiddle() throws Exception {
        Double[] arrays = {2.1, 4.2, 3.5};
        Double result = SimpleMethod.getMiddle(arrays);
        assert result==4.2;
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme