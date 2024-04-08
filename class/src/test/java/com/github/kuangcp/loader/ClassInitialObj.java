package com.github.kuangcp.loader;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 2019-04-15 12:08 AM
 */
@Slf4j
public class ClassInitialObj {

    @Test
    public void testLoad1() throws ClassNotFoundException {
        String path = "com.github.kuangcp.loader.InitialTest";

        Class<?> clazz = InitialObj.class;
        log.debug("InitialTest.class {}", clazz.getSimpleName());

        clazz = Thread.currentThread().getContextClassLoader().loadClass(path);
        log.debug("classLoader.loadClass {}", clazz.getSimpleName());

        clazz = Class.forName(path);
        log.debug("Class.forName {}", clazz.getSimpleName());
    }

    @Test
    public void testFieldRef() throws Exception {
        System.out.println(InitialObj.sb);
    }
}

