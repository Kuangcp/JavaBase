package com.github.kuangcp.block;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by https://github.com/kuangcp on 17-8-15  上午9:41
 *
 * CopyOnWriteList 的应用案例
 * 迭代时加锁 保证了此次迭代过程不受影响，但是读取到的数据并不是实时的，
 * 而是读取加锁那一刻的数据快照，这就会导致每次读取的数据都可能不同
 *
 * 这里的锁加不加无所谓，因为他们是各自读取的副本 因为这个类的特性，不加锁也能正常运行，
 * 没有并发读取的事情发生，加不加锁都会出现只有第二个线程有输出的现象：
 * 为什么只有第二个线程有输出？？？ 加不加锁都会出现的问题就是获取的迭代器是同一个对象，
 * 因为多线程的情况下调用同一个方法会有这种情况,因为不同步，所以还是要加上关键字么？？？
 *
 * *****************************
 * 不加锁的情况下  这是个什么输出？？？？ 猜测是因为线程的竞争，同时输出的结果
 * 猜测是 先是输出了名字，然后应该输出换行的时候，被第二个线程抢占了输出，然后输出了第一个元素后，该线程输出的换行，然后输出了第二个线程的所有的信息就结束了
 * a: a: Element{phone='2'}, Element{phone='3'}, Element{phone='4'}, Element{phone='5'},
 * Element{phone='1'},
 *
 * 这是按正常的思路运行正常的结果，这个类就以这两种结果，交替输出。。。。。。
 * a: Element{phone='1'}, Element{phone='2'}, Element{phone='3'}, Element{phone='4'},
 * a: Element{phone='1'}, Element{phone='2'}, Element{phone='3'}, Element{phone='4'}, Element{phone='5'},
 * *************************
 * 加锁的情况下的 怪异输出是这样的，和上面的不加锁的信息一致，但是顺序乱了，说明了锁 保证了两个线程的先后性
 * a: Element{phone='1'}, Element{phone='2'}, Element{phone='3'}, Element{phone='4'}, Element{phone='5'},
 * a:
 *
 *
 * 这一般需要很大的开销，但是当遍历操作的数量大大超过可变操作的数量时，这种方法可能比其他替代方法更 有效。在不能或不想进行同步遍历，
 * 但又需要从并发线程中排除冲突时，它也很有用。“快照”风格的迭代器方法在创建迭代器时使用了对数组状态的引用。此数组在迭代器的生存期内不会更改，
 * 因此不可能发生冲突，并且迭代器保证不会抛出 ConcurrentModificationException。创建迭代器以后，迭代器就不会反映列表的添加、移除或者更改。
 * 在迭代器上进行的元素更改操作（remove、set 和 add）不受支持。这些方法将抛出 UnsupportedOperationException。
 *
 *
 * 如果在迭代输出的过程中，加上睡眠时间，猜测 第二个线程就因为锁没有释放得不到数据，锁失败了就放弃了，不等待么
 * 可是锁成功了呀？？？ 是迭代器获取失败？
 * 原来得到的是一个迭代器。。因为迭代器是单向指针，迭代一次就无法再次使用了，
 * 所以拿到迭代器的第二次想要迭代的线程 当然是没有数据的，
 */
public class CopyOnWriteDemo {

  public static void main(String[] s) {
    CopyOnWriteArrayList<Element> elements = new CopyOnWriteArrayList<>();
    ReentrantLock lock = new ReentrantLock();
    ElementList list = new ElementList(elements, lock, "list > ");

    // 两个独立的线程分别加锁并得到了副本，所以运行得到的结果是不同的
    new Thread(() -> {
      list.addElement(new Element("1"));
      list.addElement(new Element("2"));
      list.addElement(new Element("3"));
      list.addElement(new Element("4"));
      list.prep();
      list.listElement("th1 : ");
    }).start();

    new Thread(() -> {
      list.addElement(new Element("5"));
      list.prep();
      list.listElement("th2 : ");
    }).start();

  }
}

class ElementList {

  private final CopyOnWriteArrayList elements;
  private final ReentrantLock lock;
  private final String name;
  private Iterator it;

  ElementList(CopyOnWriteArrayList elements, ReentrantLock lock, String name) {
    this.elements = elements;
    this.lock = lock;
    this.name = name;
  }

  void addElement(Element ele) {
    elements.add(ele);
  }

  void prep() {
    it = elements.iterator();//设置迭代器
    // 为什么两个线程会得到同一个迭代器
    // 经过groovy中验证，多个线程调用这个得到迭代器的方法的时候会有偶尔出现 返回相同迭代器对象的情况发生
//        System.out.println("得到迭代器"+it);
  }

  void listElement(String who) {
    lock.lock(); // 进行迭代的时候进行 锁定 ，
    System.out.println(who + lock.isLocked() + "遍历 " + it);
    try {
      // 如果这样写的话，就必然只有一个迭代器，因为it是共享的，这是一个属性，
//            if (it ==null){
//                it = elements.iterator();
//            }
      if (it != null) {
        System.out.print(who + name + ": ");
        while (it.hasNext()) {
//                    System.out.println(it);
          System.out.print(it.next() + ", ");
        }
        System.out.println("换行");
      }
    } finally {
      lock.unlock();
    }
    System.out.println();
    // try catch finally 这个结构下来，catch多行注释掉，会影响finally ？？
  }
}

@Data
@AllArgsConstructor
class Element {

  private String phone;
}