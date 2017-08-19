package com.concurrents.forkjoin

import java.util.concurrent.ForkJoinPool
import java.util.concurrent.RecursiveAction

/**
 * Created by https://github.com/kuangcp on 17-8-18  下午9:46
 * 使用ForkJoin的简单例子，但是没看懂。
 */
class Element implements Comparable{
    Long id;

    Element(Long id) {
        this.id = id
    }
    @Override
    int compareTo(Object other) {
        return this.id - ((Element)other).id
    }

    @Override
    public String toString() {
        return "Element{" +
                "id=" + id +
                '}';
    }
}
class ElementSorter extends RecursiveAction{

    // 串行排序数量么
    private static final int SMALL_ENOUGH = 32
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
    void merge(ElementSorter left, ElementSorter right){
        int i = 0
        int lCt = 0
        int rCt = 0
        while (lCt < left.size() && rCt <right.size()){
            result[i++] = (left.result[lCt].compareTo(right.result[rCt]) < 0)?
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
            int mid = size() / 2
            ElementSorter left = new ElementSorter(elements, start, start+mid)
            ElementSorter right = new ElementSorter(elements, start+mid, end)
            invokeAll(left, right)
            merge(left, right)

        }
    }
}

// 生成数据
lu = new ArrayList<>()
text = ""
for (i=0; i<20; i++){
    element = new Element(System.currentTimeMillis())
    lu.add(element)
    sleep(2)
}
// 打乱顺序
Collections.shuffle(lu)
for(Element el:lu){
    println(el.toString())
}
println("排序后")
elements = lu.toArray(new Element[0])// 传入空数组，省掉空间分配
ElementSorter sorter = new ElementSorter(elements as Element[])

ForkJoinPool pool = new ForkJoinPool(4)
pool.invoke(sorter)
for (Element element: sorter.getResult()){
    println(element.toString())
}
