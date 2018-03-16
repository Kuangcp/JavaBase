package com.github.kuangcp.schedule

import java.util.concurrent.Callable
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Created by https://github.com/kuangcp on 17-8-18  下午3:59
 * 任务建模的三种方法 Callable Future FutureTask
 * Callable 是SAM类型（单一抽象方法） 的示例， 这是Java把函数作为一等类型的可行方法
 * Future接口 用来表示异步任务，
 *      - get() 用来获取结果，如果结果还没准备好就会阻塞直到它能去到结果，有一个可以设置超时的版本，这个版本永远不会阻塞
 *      - cancel() 运算结束前取消
 *      - isDone() 调用者用它来判断运算是否结束
 */
//////////////////
list = new ArrayList<>()
list.add(12)
list.add(134)

Callable<String> cb = new Callable<String>() {
    @Override
    String call() throws Exception {
        return list.toString()
    }
}
println(cb.call())

//////////////////////////////
// 这里需要封装一个方法得到该对象，这个方法是处于多线程的环境下
Future<Long> fut = null

Long result = null
while (result != null){
    try{
        result = fut.get(60, TimeUnit.MILLISECONDS)
    }catch(TimeoutException e){

    }
    println("Still not found the billionth prime!")

}
println("Found it : "+result.longValue())
