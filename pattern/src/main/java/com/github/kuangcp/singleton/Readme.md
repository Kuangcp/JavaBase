# 单例模式
推荐:
- 双重校验 (DLC: Double Lock Check) 并加上 volatile 修饰
- 静态内部类
- 枚举

https://www.cnblogs.com/zhaoyan001/p/6365064.html

饿汉式: 静态常量, 静态代码块, 枚举
懒汉式: 静态内部类

以上除了 加强的DLC的方式 都是属于借助Class类加载机制实现线程安全
因为 ClassLoader 中 loadClass 方法是 synchronized 修饰的, 除非被重写
所以即使没有显式的使用 synchronized, 但是底层实现上使用了
