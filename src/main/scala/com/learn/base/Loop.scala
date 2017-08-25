// 几种循环
// 条件for循环
for (i <- 1 to 10; if i%2 ==0){
    println(i)
}
// 多变量循环
for(x<- 1 to 16; y<- 1 to x){
    // 像Python一样的有 字符串*N 的语法
    println(" "*(x-y) + x.toString * y)
}
// 一次新建，多次使用
val xs = for(x <- 2 to 11) yield x
for(factx <- xs) println(factx)