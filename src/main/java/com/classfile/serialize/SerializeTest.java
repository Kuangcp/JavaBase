package com.classfile.serialize;

import java.io.*;

/**
 * Created by https://github.com/kuangcp on 17-10-24  下午2:27
 * 使用jdk自带的Serializable接口序列化对象 然后反序列化对象
 * 意义在于能够让反射更为准确，装载对象？？
 * @author kuangcp
 */
public class SerializeTest {
    public static void main(String[]s){
        out();
        in();
    }

    /**
     * 使用JDK的接口 序列化对象 并输出
     */
    public static void out(){
        Person person = new Person();
        person.setAddress("12");
        person.setName("myth");
        person.setPhone("123456654");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("/home/kcp/test/person.md");
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(person);
            out.close();
            fileOutputStream.close();
            System.out.println("序列化完成");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 反序列化
     */
    public static void in(){

        Person person = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("/home/kcp/test/person.md");
            ObjectInputStream in  = new ObjectInputStream(fileInputStream);
            person = (Person)in.readObject();
            in.close();
            fileInputStream.close();
            System.out.println(person.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
