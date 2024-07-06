package com.github.kuangcp.instantiation;

/**
 * 看起来 count 似乎是先使用再声明的 静态变量是类加载时期就分配到了数据区, 在内存中只有一个, 不会分配多次, 其后所有的操作都是值改变, 地址不会变 ?? 类初始化的时候,
 * 先去查找类中的所有静态声明, 然后分配空间, 这时候只有空间, 还没有赋值, 然后依据代码的顺序执行, 进行赋值 IDEA 里面避免这个问题是比较容易的, 会有明显的警告
 *
 * @author <a href="https://github.com/kuangcp">Kuangcp</a>
 * @date 2019-05-15 09:03
 */
public class StaticFieldInit {

    static int num = 1; // 这个值被覆盖

    static {
        num = 2;
        count = 2;
        // 这个值被覆盖, 看起来似乎是先使用再声明
        // 由于按顺序执行, 所以 count = 2 count = 1, 最终为1
    }

    static int count = 1;

}
