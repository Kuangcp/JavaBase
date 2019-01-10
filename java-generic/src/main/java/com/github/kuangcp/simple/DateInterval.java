package com.github.kuangcp.simple;

import java.util.Date;

/**
 * Created by https://github.com/kuangcp on 18-1-12  下午10:00
 * 继承泛型类:
 * 最终事实:
 * 虚拟机中没有泛型,只有普通的类和方法,
 * 所有的类型参数都用他们的限定类型替换
 * 桥方法被合成来保持多态
 * 为保证类型安全性,必要时,插入强制类型转换
 *
 * @author kuangcp
 */
public class DateInterval extends Pair<Date> {

  // 设定时间区间,并确保后面时间大于等于前面时间,保证时间区间合理性
  public void setSecond(Date date) {
    if (date.compareTo(getFirst()) >= 0) {
      super.setSecond(date);
    }
  }
  // 经过类型擦除后,查看字节码反编译的类文件可以看到:
  // 会有俩setSecond方法,一个是继承过来的,一个是子类所有
  // 继承过来的方法 其入参是Object类型
  // 如果
//    {
//        DateInterval val = new DateInterval();
//        Pair<Date> pair = val;
//        pair.setSecond(new Date());
//    }
  // 那么这里的调用就应该具有多态性,并调用最合适的那个,但是因为pair对象本质是子类对象,所以肯定是调用子类的Date入参的那个方法
  // 所以为了解决这个问题, 就在子类中生成桥方法来解决
//    public void setSecond(Object date){
//        setSecond((Date)date);
//    }
  // 那么其执行过程如下:
  // 变量pair是声明为 Pair<Date>,并且该类具有一个入参为Object的setSecond方法,当虚拟机调用这个方法的时候,这个对象是子类类型的
  // 所以就会调用子类的setSecond(Object)方法,而这个方法就是自动加进去的桥方法,实际上还是调用了子类setSecond(Date)方法

  // 再思考一步:
//    setSecond这个在子类中出现原方法和桥方法并存是没有问题的,但是如果是getSecond() 那么桥方法就会是
//    Date getSecond() 但是父类又已经有了 Object getSecond()方法,那么这里就出现了问题,在平常代码编写中是不允许这样的
//    但是因为虚拟机是通过 参数类型和返回值来确定一个方法的,所以运行是允许的,编译前是不允许的


}
