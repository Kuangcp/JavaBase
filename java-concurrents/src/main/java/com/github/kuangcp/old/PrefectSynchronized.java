package com.github.kuangcp.old;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by https://github.com/kuangcp on 17-8-13  下午7:23
 *
 * 完全同步类 所有的属性都是私有的，方法也是私有的
 */
public class PrefectSynchronized {
    // 可以在声明final不初始化，但是一定要在构造器中初始化
    private final String identifier;

    private final Map<String, Long> arrivalTime = new HashMap<>();

    public PrefectSynchronized(String id){
        identifier = id;
    }

    public synchronized String getIdentifier(){
        return identifier;
    }

    public synchronized void updata(String update){
        long time = System.currentTimeMillis();
        arrivalTime.put(update, time);
    }

    public synchronized boolean confirm(String update){
        Long time = arrivalTime.get(update);
        return time != null;
    }
}
