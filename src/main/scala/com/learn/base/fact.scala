// 求阶乘，怎么非要扯到 -1 ？
def fact(base: Int) : Int = {
    if(base < 0)
        println("负数没有阶乘")
        return 1
    if(base == 0)
        return 1
    else
        return base * fact(base - 1)
}
println(fact(-1))