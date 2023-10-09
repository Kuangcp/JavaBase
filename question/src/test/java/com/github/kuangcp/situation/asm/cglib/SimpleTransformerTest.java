package com.github.kuangcp.situation.asm.cglib;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp
 */
@Slf4j
public class SimpleTransformerTest {

    @Test
    public void testInit() {
        // 虽然该类属性是 static final 但是第一次引用的时候才进行加载和初始化
        new Thread(() -> {
            try {
                SimpleTransformer instance = SimpleTransformer.INSTANCE;
                log.info("instance={}", instance);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }).start();

        // 第二次调用就直接报错 NoClassDefFoundError
        log.info("instance={}", SimpleTransformer.INSTANCE);
    }
}