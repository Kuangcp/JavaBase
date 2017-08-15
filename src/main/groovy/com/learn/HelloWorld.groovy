package com.learn

/**
 *
 * Created by Myth on 2017/4/3
 * 1 测试Groovy的配置是否通过
 * 2 实例化对象时，如果传递进去的是一个变量的引用，那么这个变量后续的变化会影响到对象的属性么
 *      结论是 不会
 * 3 迭代器迭代完后，对象的状态是怎样的，迭代完了就销毁了所有的数据么
 *      结论是 因为是单向指针，只能用一次，所以迭代器被两次引用的话，后者是没有数据的
 */

// 1
//println("Hello Groovy ")
//println("""
//这个就是 Python的多行字符串
//语法很多是一样的
//""")

// 2
//class Person{
//    String name;
//
//    Person(String name) {
//        this.name = name
//    }
//
//    @Override
//    public String toString() {
//        return "Person{" +
//                "name='" + name + '\'' +
//                '}';
//    }
//}
//
//name = "89898989"
//p1 = new Person(name)
//println(p1.toString())
//name = "7878"
//println(p1.toString())

// 3

list = new ArrayList<>();
for(i=0;i<10;i++){
    list.add("list"+i)
}
//println(list)
it = list.iterator()
println("得到的迭代器 "+it)
while (it.hasNext()){
    print(it.next()+"  ")
}
println()
while (it.hasNext()){
    print(it.next()+"  ")
}


