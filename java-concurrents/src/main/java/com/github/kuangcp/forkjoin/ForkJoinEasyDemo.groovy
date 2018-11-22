package com.github.kuangcp.forkjoin

import java.util.concurrent.ForkJoinPool
/**
 * Created by https://github.com/kuangcp on 17-8-18  下午9:46
 * TODO 使用ForkJoin的简单例子，但是没看懂
 */

// 生成数据
List lu = new ArrayList<Element>()
for (int i = 0; i < 12; i++) {
  Element element = new Element(System.currentTimeMillis())
  lu.add(element)
  sleep(2)
}
// 打乱顺序
Collections.shuffle(lu)
for (Element el : lu) {
  println(el.toString())
}
elements = lu.toArray(new Element[0])// 传入空数组，省掉空间分配
ElementSorter sorter = new ElementSorter(elements as Element[])

// 使用任务池封装一层再调用，或者直接运行方法都可以
//sorter.compute()
ForkJoinPool pool = new ForkJoinPool(4)
pool.invoke(sorter) // StackOverflowError 这个可能是因为数据量太小了会出现

println("排序后")
for (Element element : sorter.getResult()) {
  println(element.toString())
}