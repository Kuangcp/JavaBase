package com.net.socket.onechatone;

import java.util.*;

/**
 * Created by Myth on 2017/4/2
 *
 */
public class ChatMaps<K,V> {
   //创建一个线程安全的HashMap
    public Map<K,V> map = Collections.synchronizedMap(new HashMap<K, V>());

    //根据Value来删除指定项
    public synchronized void removeByValue(Object value){
        for(Object key:map.keySet()){
            if(map.get(key) == value){
                map.remove(key);
                break;
            }
        }
    }
    //获取所有Value组成的set集合中
    public synchronized Set<V> valueSet(){
        Set <V> result = new HashSet<V>();
        //将Map中所有Value添加到result集合中
        //java8表达式：
        map.forEach((key,value)->result.add(value));
        return result;
    }
    //根据value查找key
    public synchronized K getKeyByValue(V val){
        //遍历所有key组成的集合
        for (K key:map.keySet()){
            //如果指定key对应的value与被搜索的value相同，则返回相同的key
            if(map.get(key) == val || map.get(key).equals(val)){
                return key;
            }
        }
        return null;
    }
    //实现put方法，该方法不允许value重复
    public synchronized V put(K key,V value){
        for(V val:valueSet()){
            //如果放入的value是有重复的，就抛出异常
            if(val.equals(value) && val.hashCode() == value.hashCode()){
                throw new RuntimeException("MyMap实例中不允许有重复value！");
            }
        }
        return map.put(key,value);
    }
}
