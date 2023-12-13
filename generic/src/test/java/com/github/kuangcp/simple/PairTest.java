package com.github.kuangcp.simple;

import com.github.kuangcp.common.Human;
import com.github.kuangcp.common.Junior;
import com.github.kuangcp.common.Student;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Date;

/**
 * Created by https://github.com/kuangcp on 18-1-11  下午5:38
 * 这个泛型类的使用 和 集合的泛型使用 用法一致
 *
 * @author kuangcp
 */
@Slf4j
public class PairTest {

  // Junior -> Student -> Human
  @Test
  public void testBasicGeneric() {
    Pair<Date> pair = new Pair<>();
    pair.setFirst(new Date());
    System.out.println(pair.getFirst());

    // 编译期 泛型类型检查, Pair的泛型类型已经声明为Date, 所以以下编译通不过
//  x  pair.setSecond(new Integer(3));
  }

  @Test
  public void testUsePair() {
    Float[] arrays = {2.1f, 4.2f, 3.5f, 5.5f, 2.11f};
    Pair<Float> pair = Pair.minAndMax(arrays);
    log.info("min={} max={}", pair.getFirst(), pair.getSecond());

    String[] data = {"ddd", "d", "aa"};
    log.info("result={}", Pair.minAndMax(data));
    log.info("result={}", Pair.middle(data));
  }

  /**
   * 使用 ? extends XXX 声明的泛型容器 只能get 不能set(编译通不过)
   * 且 适用于 方法的参数, 不适用于返回值
   *
   * 因此如果你想从一个数据结构里获取数据，使用 ? extends 通配符 限定通配符总是包括自己
   */
  @Test
  public void testExtends() {
    Pair<Human> humans = new Pair<>();
    // Human 自身 以及 他的子类都能放入
    humans.setFirst(new Junior());
    humans.setFirst(new Human());
    humans.setSecond(new Student());
//  x  humans.setSecond(new Object());
    paramWithSuper(humans);

    // Student 自身 以及 他的子类都能放入
    Pair<? extends Student> classmates = new Pair<>();
    paramWithExtends(classmates);

    Pair<? extends Student> result = returnWithExtends();
    log.info("result={}", result);
  }

  /**
   * @param student ? extends Human的子类 都是能放入的
   */
  private void paramWithExtends(Pair<? extends Student> student) {
//    student.setFirst(new Human());
//    student.setSecond(new Student());
//    student.setFirst(new Junior());
//    student.setSecond(new Object());
    log.info("first={} second={}", student.getFirst(), student.getSecond());
  }

  private Pair<? extends Student> returnWithExtends() {
    Pair<Student> studentPair = new Pair<>();
    studentPair.setFirst(new Student());
    studentPair.setSecond(new Junior());

//    Pair<Human> humanPair = new Pair<>();
//    return humanPair;
    return studentPair;
  }

  /**
   * ? super xxx 只能set, 不能get(丢失了泛型)
   * 因此如果你想把对象写入一个数据结构里，使用 ? super 通配符。限定通配符总是包括自己
   */
  @Test
  public void testSuper() {
    Pair<Human> human = new Pair<>();
    human.setFirst(new Human());
    human.setSecond(new Junior());
    paramWithSuper(human);

    Pair<Junior> juniorPair = new Pair<>();
//    paramWithSuper(juniorPair);

    Pair<? super Student> result = returnWithSuper();
    // 因为 result 是约束为能放入Student以及他的超类,但是又是不明确的超类, 所以这个result能放入 Student子类, 因为多态
    // 但是不能放入 Human 因为不明确result约束的超类到底是哪个, 即使Human是Student超类, 不能放入
    result.setSecond(new Junior());
//    result.setSecond(new Human());

    log.info("result={}", result);
  }

  private void paramWithSuper(Pair<? super Student> student) {
    student.setFirst(new Junior());
//    student.setFirst(new Human());
//    student.setFirst(new Object());

    log.info("{}", student);
  }

  private Pair<? super Student> returnWithSuper() {
    Pair<Human> humanPair = new Pair<>();
    humanPair.setFirst(new Human());

    Pair<Junior> juniorPair = new Pair<>();
    juniorPair.setFirst(new Junior());
//    return juniorPair;
    return humanPair;
  }

  /**
   * 原始类型的坑
   */
  @Test
  public void testPrimitiveType() {
    Pair<? extends Human> classmates = new Pair<>();
    ((Pair) classmates).setFirst("str");
    log.info("{}", classmates.getFirst());

    ((Pair<Human>) classmates).setFirst(new Human("name"));

    // 虽然通过了编译,也运行正常,但是该泛型程序没有实现其需要的目的(类型约束)
    // 原本应该是Human对象才能set,但是变成原始类型即多态后就能set任意对象了
    log.info("{}", classmates.getFirst());
  }

  // 无限定通配符
  @Test
  public void testHasNull() {
    Pair<Human> humanPair = new Pair<>();
    humanPair.setSecond(new Human("fds"));

    log.info("result={}", hasNull(humanPair));
    log.info("result={}", hasNulls(humanPair));
  }

  /**
   * 判断 Pair 是否有属性为null
   *
   * @param p 泛型变量约束的类, 不需要实际的类型
   */
  private boolean hasNull(Pair<?> p) {
    // get方法返回值只能返回给Object, set方法不能被调用, 甚至不能用Object调用
    return p.getFirst() == null || p.getSecond() == null;
  }

  /**
   * 在这个场景下 T 和 ? 使用效果是一致的, 但是 ? 不能使用 set, T 可以
   */
  private <T> boolean hasNulls(Pair<T> p) {
    return p.getSecond() == null || p.getFirst() == null;
  }
}