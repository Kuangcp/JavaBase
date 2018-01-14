package com.generic.simple;

import org.apache.poi.ss.formula.functions.T;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.Date;

import static org.mockito.Mockito.*;

/**
 * Created by https://github.com/kuangcp on 18-1-11  下午5:38
 * 这个泛型类的使用就有点像集合的泛型使用了
 * 理解为模板类?
 * @author kuangcp
 */
public class PairTest {
    @Test
    public void testsimple(){
        Pair<Date> pair = new Pair<>();
        pair.setFirst(new Date());
        System.out.println(pair.getFirst());

        // 因为实例化这个类的时候就已经声明了类型为Date,所以里面的所有的T都会替换成Date
        // 下面再用别的参数的话就会编译报错,所以用来约束类型很方便
//        pair.setSecond(new Integer(3));

    }

    @Test
    public void testMinMax(){
        Float[] arrays = {2.1f, 4.2f, 3.5f};
        Pair<Float> pair = new Pair<>();
        pair = pair.minmax(arrays);
        System.out.println(pair.getFirst()+"--"+pair.getSecond());
    }
    class Human {

    }
    class Student extends Human{

    }
    class Junior extends Student{

    }

    /**
     * 使用extends通配就不能set能get
     */
    @Test
    public void testExtends(){
        Pair<Human> humans = new Pair<>();

        // 使用泛型通配符,达到类型约束的目的
        Pair<? extends Human> classmates = humans;
//        classmates.setSecond(new Student()); //报错
//        classmates.setSecond(new Object()); //不能set
        Human human = classmates.getSecond();

        // 原始类型的坑
        Pair pair = humans; // 强转为原始类型
        pair.setFirst(new String("fdsfds"));
        // 虽然通过了编译,也运行正常,但是该泛型程序没有实现其需要的目的(类型约束)
        // 原本应该是Human对象才能set,但是变成原始类型即多态后就能set任意对象了
        System.out.println(humans.getFirst());
    }

    /**
     * 使用super通配就不能get能set
     */
    @Test
    public void testSuper(){
        Student student = new Student();
        Human human = student;// 子类可以赋值给父类

//        Human human1 = new Human();
//        Student student1 = human1; 父类不能赋值给子类


        Pair<Student> students = new Pair<>();
//        Pair<? super Student> pair = students;
//        pair.setFirst(new Student()); // 能够set 约束类型或子类
//        pair.setFirst(new Junior());

        students.setFirst(new Junior()); // Pair<Junior> 可以转成 Pair<Student>
//        Junior junior = students.getFirst(); get失败 只能用Student接收

        Pair<Human> humanPair = new Pair<>();
//        Pair<Student> classmates = humanPair;
        humanPair.setFirst(new Junior());
//        Junior junior = humanPair.getFirst(); get失败只能用Human接收


//        pair.setFirst(new Human());
//        pair.setFirst(new Object());// 不能set 父类

//        Human human = pair.getFirst(); 不能get 只能用Object接收,失去了泛型的作用
//        pair.setFirst(new Human()); //编译报错




        Pair<Human> humans = new Pair<>();
        Student[] list = {new Student(), new Student()};
        minMaxBonus(list, humans); // 又能放父类???

    }

    /**
     * 通配符类型的 超类型限定的 类型变量 约束
     * @param list 资源
     * @param result 使用了通配符的超类型的泛型约束  该对象 不能get 只能set
     */
    public void minMaxBonus(Student[] list, Pair<? super Student> result){
        result.setFirst(list[0]);
        result.setSecond(list[0]);
//        Student student = result.getSecond();
//        Human human = result.getSecond();
    }

    /**
     * 通配符类型的 类型变量 的约束
     * @param human 使用了通配符限定的泛型约束的 参数 该对象 不能set 只能get
     */
    public void printMessage(Human obj, Pair<? extends Human> human){
        Human first = human.getFirst();
//        human.setSecond(obj);
        System.out.println(first);
        //        Student second = human.getSecond(); // 正常: 限定了是Human子类
    }
}