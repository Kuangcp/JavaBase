package com.github.kuangcp.forkjoin

import java.util.concurrent.ForkJoinPool
import java.util.concurrent.RecursiveAction

/**
 * Created by https://github.com/kuangcp on 17-8-18  下午9:46
 * 使用ForkJoin的简单例子，但是没看懂。
 */
class Element implements Comparable{
    Long id

    Element(Long id) {
        this.id = id
    }
    @Override
    int compareTo(Object other) {
        return this.id - ((Element)other).id
    }

    @Override
    String toString() {
        return "Element{" +"id=" + id +'}'
    }
}

// 继承这个类才可以看成是一个task
class ElementSorter extends RecursiveAction{

    // 这是一个标记量，当排序元素低于这个数量就用 Arrays.sort() 排序，大于就用归并
    private static final int SMALL_ENOUGH = 10
    private final Element [] elements
    private final int start, end
    private final Element [] result

    ElementSorter(Element [] elements){
        this(elements, 0 , elements.length)
    }
    ElementSorter(Element[] elements, int start, int end) {
        this.elements = elements
        this.start = start
        this.end = end
        this.result = new Element[elements.length]
    }
    // 合并排序
    void merge(ElementSorter left, ElementSorter right){
        int i = 0
        int lCt = 0
        int rCt = 0
        while (lCt < left.size() && rCt <right.size()){
            result[i++] = left.result[lCt] < right.result[rCt] ?
            left.result[lCt++]:right.result[rCt++]
        }
        while(lCt < left.size()){
            result[i++] = left.result[lCt++]
        }
        while(rCt < right.size()){
            result[i++] = right.result[rCt++]
        }
    }
    int size(){
        return end - start
    }
    Element[] getResult() {
        return result
    }
    // 父类声明的抽象方法
    @Override
    protected void compute() {
        if(size() < SMALL_ENOUGH){
            System.arraycopy(elements, start, result, 0, size())
            Arrays.sort(result, 0, size())
        }else{
            int mid = (int) (size() / 2)
            ElementSorter left = new ElementSorter(elements, start, start+mid)
            ElementSorter right = new ElementSorter(elements, start+mid, end)
//            执行处，将两个task执行，这两个task的任务就是递归的拆解整个数组，然后进入下面的合并排序里
            invokeAll(left, right)
            // 调用会报错,因为没有没有对象（或者说 任务单元的资源）去调用这个方法，
            // StackOverflowError 这是啥，栈溢出
//            invoke()
            merge(left, right)

        }
    }
}

// 生成数据
List lu = new ArrayList<Element>()
for (int i=0; i<12; i++){
    Element element = new Element(System.currentTimeMillis())
    lu.add(element)
    sleep(2)
}
// 打乱顺序
Collections.shuffle(lu)
for(Element el:lu){
    println(el.toString())
}
elements = lu.toArray(new Element[0])// 传入空数组，省掉空间分配
ElementSorter sorter = new ElementSorter(elements as Element[])

// 使用任务池封装一层再调用，或者直接运行方法都可以
//sorter.compute()
ForkJoinPool pool = new ForkJoinPool(4)
pool.invoke(sorter) // StackOverflowError 这个可能是因为数据量太小了会出现

println("排序后")
for (Element element: sorter.getResult()){
    println(element.toString())
}