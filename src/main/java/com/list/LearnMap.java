package com.list;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by https://github.com/kuangcp on 17-8-21  下午2:48
 * Map 的常见用法
 */
public class LearnMap {

    public static void main(String[]s){
        loop();
    }

    /**
     * Map的几种迭代方式
     */
    public static void loop(){
        Map<String,String> map = new HashMap<String,String>();
        map.put("1", "a");
        map.put("2", "b");
        map.put("3", "c");

        //通过Map.keySet()遍历key和value,二次取值
        for(String key:map.keySet()){
            System.out.println(key + map.get(key));
        }

        //通过Map.entrySet()遍历key和value(推荐使用)
        for(Map.Entry<String, String> entry:map.entrySet()){
            System.out.println(entry.getKey() + entry.getValue());
        }

        //通过Map.entrySet()使用iterator()遍历key和value
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, String> entry = iterator.next();
            System.out.println(entry.getKey() + entry.getValue());
        }
    }
}
