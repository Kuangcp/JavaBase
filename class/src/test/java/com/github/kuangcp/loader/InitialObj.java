package com.github.kuangcp.loader;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Kuangcp
 * 2024-04-07 14:49
 */
@Slf4j
public class InitialObj {

    public static final StringBuilder sb = new StringBuilder();
    private static final InitialObj test = new InitialObj();

    // 需要注意类被加载进JVM才会执行，类加载是惰性的，在入口代码以及后续线程调用到该类的任意方法,引用任意属性才会触发
    // 而且 static final 如果是基本类型会被替换为常量，所以其他类在引用此类属性时，其实不会加载该类
    // static 代码块晚于 static 属性初始化完成
    static {
        log.info("static block");
    }

    public InitialObj() {
        log.info("constructor block");
    }
}
