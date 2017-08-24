package com.classfile;


import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by https://github.com/kuangcp on 17-8-24  上午11:17
 * Java调用Groovy代码的三种方式 使用GroovyShell  使用GroovyClassLoader 使用GroovyScriptEngine
 *
 */
public class JavaUseGroovy {

    public static void main(String []s){
        quoteByShell();
//        quoteByLoader();
        quoteByEngine();
    }
    /**
     * 直接执行Groovy命令行  GroovyShell
     */
    private static void quoteByShell(){
        Binding binding = new Binding();
        binding.setVariable("x", 3.4);
        binding.setVariable("y", 8.2);
        GroovyShell shell = new GroovyShell(binding);
        Object value = shell.evaluate("x + y");
        System.out.println(value);
        assert value.equals(new BigDecimal(11.6));
    }

    /**
     * 加载配置文件一样的加载脚本文件，然后像反射一样的调用里面定义的方法
     * 这种形式的调用不允许脚本形式，只能是类形式
     */
    private static void quoteByLoader(){
        GroovyClassLoader loader = new GroovyClassLoader();
        try{
            // 这路径有点坑啊
            String path = "JavaBase/src/main/java/com/classfile/OriginGroovy.groovy";
            Class<?> groovyClass = loader.parseClass(new File(path));

            GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();
            ArrayList<Integer> numbers = new ArrayList<>();
            numbers.add(23);
            numbers.add(343);
            Object[] arguments = {numbers};
            Object value = groovyObject.invokeMethod("getMax", arguments);
            System.out.println(value);
            assert value.equals(343);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /**
     * 这个的加载就是脚本形式，脚本形式中夹杂类也是可以的
     */
    private static void quoteByEngine(){
        try {
            String[] roots = new String[] { "JavaBase/src/main/java/" };
            GroovyScriptEngine gse = new GroovyScriptEngine(roots);

            Binding binding = new Binding();
            binding.setVariable("name", "Gweneth");

            Object output = gse.run("com/classfile/OriginGroovy.groovy", binding);
//            assert output.equals("Hello Gweneth");
        } catch (IOException | ResourceException | ScriptException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }
}
