package com.learn.base

/**
 * Created by https://github.com/kuangcp on 17-8-22  上午10:08
 * Groovy 的变量作用域
 *      绑定域： 没有声明类型，没有被代码块包住 全局的
 *      本地域： 声明了类型，或者def声明。或者在代码块里
 *
 *  脚本中不能使用static关键字，类中就可以
 *  脚本中方法不能命名为run
 */
class VariableScopes {
    def static d = "class static variable"

    def static run(){
        println(d)
    }
    static void main(String[]s){
        run()
    }


}

// 绑定域 全局有效
vars = 1

// 本地域 只在脚本顶层有效，在代码块里无效
String  he = "Groovy"
def var2 = 23

println(var2)
println(he)


def runs(){
    // 本地域 代码块中有效，脚本顶层无效
    def ui = "12"
    String d = 90
    println(ui+d+vars)
//    会报错
//    println(he)
//    println(var2)
}

runs()
VariableScopes.main()