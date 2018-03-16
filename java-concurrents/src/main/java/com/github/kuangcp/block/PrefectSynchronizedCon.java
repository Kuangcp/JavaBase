package com.github.kuangcp.block;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by https://github.com/kuangcp on 17-8-15  上午9:31
 * 完全同步类的 ConcurrentHashMap 实现
 */
public class PrefectSynchronizedCon {
    // 可以在声明final不初始化，但是一定要在构造器中初始化
    private final String identifier;

    // 使用了并发包里的map，下面的方法就可以写 成普通方法了
    private final Map<String, Long> arrivalTime = new ConcurrentHashMap<>();

    public PrefectSynchronizedCon(String id){
        identifier = id;
    }

    public String getIdentifier(){
        return identifier;
    }

    public void updata(String update){
        long time = System.currentTimeMillis();
        // 并法Map的特殊put方法
        arrivalTime.putIfAbsent(update, time);
    }

    public boolean confirm(String update){
        return arrivalTime.get(update) != null;
    }
}
