package com.learn.base

/**
 * Created by https://github.com/kuangcp on 17-8-22  上午10:43
 * 学习作为一等值的列表和映射的使用：
 *      如果写在脚本顶层中，不会有问题，如果写在方法块中也能正常运行，需要def声明才会有智能提示。。。
 */
class LearnListAndMaps {
}

def lists() {
    def lists = ['23', 3, 45]
    println(lists)
    // 支持负索引和分片
    println(lists[2]+", "+lists[-1]+", "+lists[0..1])
    lists = []
    println(lists[2])
}

def maps() {
// 键的双引号是可选的,也就是说默认键是字符串类型的
    def maps = [Java: 10, Groovy: "easy", "Scala": "N/A"]
// 引用可以[] 也可以直接.引用
    println(maps["Java"] + maps.Scala)
    maps.Scala = 12
    println(maps)
    maps["Scala"] = 23
    println(maps)
// 清空
    maps = [:]
    println(maps)
}
lists()
maps()