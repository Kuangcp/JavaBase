package com.github.kuangcp.bytecode.aop;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.junit.Test;

/**
 *
 * @author Kuangcp 
 * 2024-03-11 18:24
 */
public class AddImplTest {
    @Test
    public void testNormal() throws Exception {
        AddImpl add = new AddImpl();
        int r = add.add(1, 2);
        System.out.println(r);
    }

    @Test
    public void testInject() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("com.github.kuangcp.bytecode.aop.AddImpl");
        CtMethod ctMethod = cc.getDeclaredMethod("add", new CtClass[]{pool.get("int"), pool.get("int")});
        ctMethod.insertBefore("System.out.println(\"insert before by Javassist: \"+a+\" \"+b);");
        ctMethod.insertAfter("System.out.println(\"insert after by Javassist\");");
        Class<?> klass = cc.toClass();
        System.out.println(klass.getName());
        Cal cal = (Cal) klass.getDeclaredConstructor().newInstance();
        int add = cal.add(1, 4);
        System.out.println(add);
    }
}
