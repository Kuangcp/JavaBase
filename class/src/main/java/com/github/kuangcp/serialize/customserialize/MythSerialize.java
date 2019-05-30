package com.github.kuangcp.serialize.customserialize;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by https://github.com/kuangcp on 17-10-24  下午2:25
 * 自定义序列化 TODO 自定义类的对象的序列化， 实现自定义的序列化接口
 *
 * @author kuangcp
 */
@Slf4j
public class MythSerialize<T> {

  public T in(Class<T> target, InputStream inputStream) {
    T object = null;
    try {
      Reader reader = new InputStreamReader(inputStream);
      BufferedReader bufferedReader = new BufferedReader(reader);
      String line = bufferedReader.readLine();
      line = line.substring(1, line.length() - 1);
      String[] result = line.split("[,:]");
      object = target.newInstance();
      Method[] methods = target.getDeclaredMethods();
      for (int i = 0; i < result.length; i += 3) {
//                System.out.println("name:"+result[i+1]);
        for (Method method : methods) {
          if (method.getName().startsWith("set") && method.getName()
              .equals("set" + result[i + 1])) {
            if ("null".equals(result[i + 2])) {
              continue;
            }
            if ("java.lang.String".equals(result[i])) {
              method.invoke(object, result[i + 2]);
            } else if ("java.lang.Long".equals(result[i])) {
              method.invoke(object, Long.parseLong(result[i + 2]));
            } else if ("java.lang.Integer".equals(result[i])) {
              method.invoke(object, Integer.parseInt(result[i + 2]));
            } else if ("java.lang.Float".equals(result[i])) {
              method.invoke(object, Float.parseFloat(result[i + 2]));
            }
            // 递归序列化？？？ 发现有自定义的类（一定要继承自定义的序列化接口）就进行序列化然后嵌套
            // 所以序列化的成功与否在于所有的属性是否都序列化
//                        method.invoke(object,filedType.cast(result[i+2]));
          }
        }
      }
    } catch (InstantiationException | IOException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return object;
  }

  public ByteArrayOutputStream out(T object) {
    try {
      Class domain = object.getClass();
      Method[] methods = domain.getDeclaredMethods();
      StringBuilder builder = new StringBuilder();
      builder.append("[");
      for (Method method : methods) {

        if (method.getName().startsWith("get")) {
          Class returnType = method.getReturnType();
          builder.append(returnType.getName())
              .append(":").append(method.getName().substring(3))
              .append(":").append(method.invoke(object))
              .append(",");
        }
      }
      String result = builder.toString().substring(0, builder.length() - 1) + "]";

      log.info("content={}", result);

      ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
      byteOutput.write(result.getBytes());
      return byteOutput;
    } catch (IOException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }
}
