package com.learn.base

import jdk.nashorn.internal.ir.annotations.Immutable

/**
 * Created by https://github.com/kuangcp on 17-8-23  下午3:33
 * 相比于Java，一些现代特性：
*    - GroovyBean，更简单的bean
     - 用操作符`?.`实现null对象的安全访问
     - 猫王操作符(Elvis operator)，更短的if/else结构
     - Groovy字符串，更强的字符串抽象
     - 函数字面值（闭包），函数当值传递
     - 对正则表达式的本地支持
     - 更简单的XML处理
 */

// *********************************************  GroovyBean
// 只有属性的就是bean
// 默认会生成setget方法，方法是public的，可以将setget方法显式声明为私有
// 属性设置私有后不会有setget方法，但是允许 . 直接访问属性

//@Immutable 如果使用该注解，意思是这个类的状态不可修改，这对于传递线程安全的数据结构很有用
class Beans{
    private String name
    private int age
    private String addr

//    Beans(name, age, addr) {
//        this.name = name
//        this.age = age
//        this.addr = addr
//    }

}
println("关于Bean")
// 自动构造方法含无参方法，当显式声明含参构造方法后就会赋值混乱。赋值是采用映射来赋值的，是任意数量和位置的和Python有点像
bean = new Beans(name:"name", age:12)
bean.name = "9090909090"
println(bean.name+bean.age+bean.addr)


// ******************************************** 安全解引用 ?.
println("关于 ?. ")
list = [null, new Beans(name:"myth"), new Beans(name:"age")]
for (def line in list){
    // 虽然说什么都不做，但是还是会正常输出，也就是说，当做调用不存在
    println(line?.name)
}

// ******************************************** 猫王操作符 ?:

// Java 原写法
name = "myth"
status = name != null ? name : "Object"
println(status)
// 简化写法
status = name ? name : "Object"
println(status)
// 猫王操作符写法 可以省略null检查
status = name ?: "Object"
println(status)


def name(){
    print("name method")
}
name = null
name ?: name()

// ******************************************** 增强型字符串
println("\n 增强型字符串 ")
// Java里的字符串只能使用双引号
// Groovy里定义字符串使用双引号或单引号都可以，GString只能用双引号
String files = 'filename'
String files2 = "filename2"

age = 12
// GString 可以使用${} 引用变量 或者 计算表达式， 如果GString之后要转型为普通字符串，里面的表达式都会替换为其计算结果
String result = "${files} is ${files2} = ${age}"
String result2 = "${files} is ${files2} = 12"
// 其中的表达式计算后被转到可以调用toString的对象上，或是函数字面值上
println(result)

// 最好不要这样使用，
println("filename is filename2 = 12" == result)
println(result == result2)
println(GString.getTypeName())

