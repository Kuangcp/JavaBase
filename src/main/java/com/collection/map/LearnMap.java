package com.collection.map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by https://github.com/kuangcp on 17-8-21  下午2:48
 * Map 的常见迭代用法
 */
public class LearnMap {

    public static void main(String[]s){
        LearnMap map = new LearnMap();
        map.loop();
    }

    /**
     * Map的几种迭代方式
     */
    private void loop(){
        Map<String, String> map = new HashMap<String,String>();
        map.put("1", "a");
        map.put("2", "b");
        map.put("3", "c");
        formal1(map);
        formal2(map);
        formal3(map);
    }

    /**
     * 通过Map.keySet()遍历key和value,二次取值
     */
    private void formal1(Map<String, String> map){
        for(String key:map.keySet()){
            System.out.println(key + map.get(key));
        }
    }

    /**
     * 通过Map.entrySet()遍历key和value(推荐使用)
     */
    private void formal2(Map<String, String> map){
        for(Map.Entry<String, String> entry:map.entrySet()){
            System.out.println(entry.getKey() + entry.getValue());
        }
    }

    /**
     * 通过Map.entrySet()使用iterator()遍历key和value
     */
    private void formal3(Map<String, String> map){
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, String> entry = iterator.next();
            System.out.println(entry.getKey() + entry.getValue());
        }
    }
}
