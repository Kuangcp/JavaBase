package com.learn.base

/**
 * Created by https://github.com/kuangcp on 17-8-22  下午1:12
 * Groovy 和Java相比的易错 小差异
 */
class Demo {

    def private name
    // 默认是public
    def age

    /**
     * @return 默认返回最后一行的表达式的值
     */
    def getName(){
        name = "kuang"
    }

    def setFields(name, age){
        this.name = name
        this.age = age
    }

    @Override
    public String toString() {
        return "Demo{" +
                "name=" + name +
                ", age=" + age +
                '}';
    }

}

demo = new Demo()

println(demo.getName())

demo.setFields("myth", 12)
println(demo.toString())

demo.setFields"myth2", 143

println(demo.toString())

// == 就是equals 以及 is的使用
demo2 = new Demo()
demo2.setFields "myth2", 143
print(demo2.toString()+("as"=="as"))
println(demo == demo2)
print(demo.is(demo2))

