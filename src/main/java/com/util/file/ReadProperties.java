package com.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
/**
 * Created by Myth on 2017/1/13 0013 - 20:51
 * TODO 读取不到文件
 */
public class ReadProperties {

    private Properties cfg = new Properties();
    public ReadProperties(){}

    /**
     * 输入的是从src开始的路径: src/main/resources/a.properties
     * @param file
     */
    public ReadProperties(String file){
        try {
            File f = new File(file);
            cfg.load(new FileInputStream(f));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public String getString (String key){
        return cfg.getProperty(key);
    }
    public int getInt(String key){
        return Integer.parseInt(cfg.getProperty(key));
    }
    public double getDouble(String key){
        return Double.parseDouble(getString(key));
    }
    public static void main(String []a){
        ReadProperties read = new ReadProperties("src/main/resources/math/SimplexMethod.properties");
        String result = read.getString("78");
//        System.out.println(result);
        try {
            //配置文件含中文需要转码
            System.out.println(new String(result.getBytes("ISO-8859-1"), "UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
