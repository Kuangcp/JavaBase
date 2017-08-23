package com.learn.base

/**
 * Created by https://github.com/kuangcp on 17-8-22  下午4:57
 * 权限修饰符：默认是public
 */

class Person{
    def private name = "myth"
    def age = 12
    def getName(){
        return name
    }
}

class AuthorityScope {
    def private name = "myth"
    def protected age = 12
//    def friendly d = 3

    def getNames(){
        age = 22
        name = '23'
    }
    static void main(String[]s){
        def scope = new AuthorityScope()
        def person = new Person()
        println(person.age+person.getName())

        println(scope.getNames()+scope.age)
    }
}


