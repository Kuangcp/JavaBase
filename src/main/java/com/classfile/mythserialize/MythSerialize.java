package com.classfile.mythserialize;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by https://github.com/kuangcp on 17-10-24  下午2:25
 * 自定义序列化 TODO 空值，类型判断
 * @author kuangcp
 */
public class MythSerialize<T> {

    public Object in(Class target, String path){
        Object object = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            Reader reader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            line = line.substring(1, line.length()-1);
            String[] result = line.split(",|:");
            Map<String, String> resultMap = new HashMap<>();
            for(int i=0;i<result.length;i+=2){
                resultMap.put(result[i], result[i+1]);
            }

            object = target.newInstance();
            Method[] methods = target.getDeclaredMethods();
            for(Method method: methods){
                if(method.getName().startsWith("set")){
                    method.invoke(object, resultMap.get(method.getName().substring(3, method.getName().length())));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return object;
    }
    public void out(T object, String path){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            Class domain = object.getClass();
            Method[] methods = domain.getDeclaredMethods();
            StringBuilder builder = new StringBuilder();
            builder.append("[");
            for(Method method: methods){
                if(method.getName().startsWith("get")){
                    builder.append(method.getName().substring(3, method.getName().length())).append(":").append(method.invoke(object).toString()).append(",");
                }
            }
            String result = builder.toString().substring(0, builder.length()-1)+"]";
            System.out.println(result);
            // 使用ObjectOutStream类输出会有奇怪的字符
            fileOutputStream.write(result.getBytes());
            fileOutputStream.close();
            System.out.println("序列化完成");
        } catch (IOException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
