// 求阶乘
def fact(base: Int) : Int = {
    // println("int : "+base)
    if(base < 0){
        print("负数没有阶乘 ：")
        return base
    }
    if(base == 0){
        return 1
    }else{
        return base * fact(base - 1)
    }
}
println(fact(10))
println(fact(-3))