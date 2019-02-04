package com.github.kuangcp.block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by https://github.com/kuangcp on 17-8-15  上午10:12
 * 普通的这个数组对象，也是和那个并发包的类一样的效果，完全是因为锁的原因，
 * 因为锁的存在，第一个线程和第二个线程的运行是有时效先后的，所以就会出现一个线程和另一个线程的结果是不一样的
 *      如果把锁去掉，就会有异常抛出：


 *  这个类iterator 和 listIterator 方法返回的的迭代器 都是快速失败的，也就是说
 *      在创建迭代器之后，除非通过迭代器自身的 remove( 或 add 方法 1.8没有了)从结构上 （除了更新元素值的其他操作都是）对列表进行修改，
 *      否则在任何时间以任何方式对列表进行修改，迭代器都会抛出 ConcurrentModificationException。
 *  快速失败并不总是可靠的，因为线程的不确定性，快速失败是尽量的做到抛出这个异常
 *
 *       注意，这个类的实现不是同步的。使用这个封装就是同步的
 *      List list = Collections.synchronizedList(new ArrayList(...));
 *
 *  同样的，这里出现别的结果也是因为获得了同一个迭代器， 然后又因为加锁，数据同步了，所以结果是好多个，
 *  例如这样的，5没有加入进去 6 却进去了
 *  a: null, Element{phone='1'}, Element{phone='6'}, Element{phone='2'}, Element{phone='3'},
    a:
 */
public class ArrayListDemo {

    public static void main(String []s){
        ArrayList<Element> elements = new ArrayList<>();
        ReentrantLock lock = new ReentrantLock();
        String name = "a";
        ElementArrayList list = new ElementArrayList(elements, lock, name);

        // 两个独立的线程分别加锁并得到了副本，所以运行得到的结果是不同的
        new Thread(new Runnable() {
            @Override
            public void run() {
                list.addElement(new Element("1"));
                list.addElement(new Element("2"));
                list.addElement(new Element("3"));
                list.prep();
                list.listElement();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                list.addElement(new Element("5"));
                list.addElement(new Element("6"));

                list.prep();
                list.listElement();
            }
        }).start();

    }
}

class ElementArrayList {
    private final ArrayList<Element> elements;
    private final ReentrantLock lock;
    private final String name;
    private Iterator<Element> it;

    public ElementArrayList(ArrayList<Element> elements, ReentrantLock lock, String name) {
        this.elements = elements;
        this.lock = lock;
        this.name = name;
    }
    public void addElement(Element ele){
        elements.add(ele);
    }
    public void prep(){
        it = elements.iterator();//设置迭代器
    }
    public void listElement(){
        lock.lock(); // 进行迭代的时候进行 锁定 ，
        try{
//            Thread.sleep(10);
            if (it != null){
                System.out.print(name + ": ");
                while(it.hasNext()){
                    Element element = it.next();
                    System.out.print(element + ", ");
                }
                System.out.println();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}