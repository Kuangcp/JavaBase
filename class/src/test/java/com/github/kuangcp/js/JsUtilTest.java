package com.github.kuangcp.js;

import org.junit.Test;

import javax.script.Invocable;

/**
 *
 * @author Kuangcp
 * 2024-03-11 17:39
 */
public class JsUtilTest {

    @Test
    public void testAdd() throws Exception {
        Invocable func = JsUtil.load("js/add.js");
        Object res = func.invokeFunction("add", 3, 5);
        System.out.println(res);
    }
}
