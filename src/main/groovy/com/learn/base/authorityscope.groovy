package com.learn.base

/**
 * Created by https://github.com/kuangcp on 17-8-22  下午4:57
 * 权限修饰符：默认是public
 */
class authorityscope {
    def private name = "myth"
    def protected age = 12
//    def default d = 3

    def getNames(){
        age = 22
        name = '23'
    }
    static void main(String[]s){
        def scope = new authorityscope()
        println(scope.getNames()+scope.age)
    }
}


