package com.github.kuangcp.forkjoin

import java.util.concurrent.ForkJoinPool

/**
 * Created by https://github.com/kuangcp on 17-8-18  下午9:46
 */

// 生成数据
def total = 1000
List lu = new ArrayList<Element>(total)
for (int i = 0; i < total; i++) {
    Element element = new Element(i + 1)
    lu.add(element)
    sleep(2)
}

// 打乱顺序
Collections.shuffle(lu)

elements = lu.toArray(new Element[0])// 传入空数组，省掉空间分配
ElementSorter sorter = new ElementSorter(elements as Element[])

println("开始排序")
// 使用任务池封装一层再调用，或者直接运行方法都可以
//sorter.compute()
ForkJoinPool pool = new ForkJoinPool(4)
pool.invoke(sorter)

for (i in 0..<total) {
    def element = sorter.getResult()[i]
    if (element.getId() != i + 1) {
        println("error " + element.toString())
    }
}

println("排序完成")
