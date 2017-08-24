package com.learn.base

import jdk.nashorn.internal.ir.annotations.Immutable

import java.util.regex.Matcher
import java.util.regex.Pattern

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
def use_beans() {
    println("关于Bean")
// 自动构造方法含无参方法，当显式声明含参构造方法后就会赋值混乱。赋值是采用映射来赋值的，是任意数量和位置的和Python有点像
    bean = new Beans(name: "name", age: 12)
    bean.name = "9090909090"
    println(bean.name + bean.age + bean.addr)
}

// ******************************************** 安全解引用 ?.
def use_index() {
    println("关于 ?. ")
    list = [null, new Beans(name: "myth"), new Beans(name: "age")]
    for (def line in list) {
        // 虽然说什么都不做，但是还是会正常输出，也就是说，当做调用不存在
        println(line?.name)
    }
}

// ******************************************** 猫王操作符 ?:
def name(){
    print("name method")
}
def use_choose() {
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

    name = null
    name ?: name()
}


// ******************************************** 增强型字符串
def use_GString() {
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
}
// ********************************************　函数字面值
println("函数字面值")
def createName(method, param){
    println(method(param))
}
// 函数字面值赋值
def sayHi={
    name -> if(name == "myth" || name == "mythos"){ //变量和处理逻辑分离
        "Hi author　"+name+"!"
    }else{
        "Hi reader　"+name+"!"
    }
}

createName(sayHi, "mythos")

// ******************************************** 内置集合操作
def use_list() {
    println("内置集合操作")
// Java 原写法
    lists = new ArrayList<>()
    lists.add("122")
    lists.add("1")
    for (line in lists) {
        println(line)
    }

// Groovy方式
    movies = ["123 ", "qw ", "rt "]
    movies.each({ print it }) // 隐含变量 it
    println()
    movies.each({ x -> print x })
}
// ******************************************** 对正则表达式的内置支持
def use_pattern() {
    println("\n正则支持")
// Java  匹配 1010 然后再替换成 0101
    Pattern pattern = Pattern.compile("1010")
    String input = "1010"
    Matcher matcher = pattern.matcher(input)
    if (input.matches("1010")) {
        input = matcher.replaceFirst("0101")
        println(input)
    }

// Groovy 方式实现
    def pattern1 = /1010/
    def input1 = "1010"
    def matcher1 = input1 =~ pattern1
    if (input1 ==~ pattern1) {
        input1 = matcher1.replaceFirst("0101")
        println(input1)
    }

// 正则表达式和函数字面值结合使用：
// 分析String得到一个人的名字和年龄 输出详细信息
    ("Hazel 1" =~ /(\w+)(\d+)/).each {full, name, age -> println "$name is $age years old!."}
}

// ******************************************** 简单XML处理
class XmlExample{
    static def PERSON =
"""
<person id='2'>
  <name>Myth</name>
  <age>1</age>
</person>
"""
}
class Person{
    def id
    def name
    def age
}

def use_xml(){
    println("简单XML处理")
    // 生成XML
    def writer = new StringWriter()
    def xml = new groovy.xml.MarkupBuilder(writer)
//    groovy.json.JsonBuilder(writer) 创建JSON
    xml.person(id:2){
        name 'Myth'
        age 1
    }
    println("生成的XML "+writer.toString())

    // 解析XML
    def xmlPerson = new XmlParser().parseText(XmlExample.PERSON) // 将文件加载进内存初始化成对象
    // 填入GroovyBean Person中
    Person p = new Person(id: xmlPerson.@id, name:xmlPerson.name.text(), age:xmlPerson.age.text())
    println("解析XML的结果 ${p.id}, ${p.name}, ${p.age}")
}

use_xml()
//use_index()
//use_list()
//use_beans()
//use_choose()
//use_GString()
//use_pattern()



