package com.learn.base

object Helloworld {
  def main(args: Array[String]): Unit ={
    val hello = "Hello world!"
    println(hello)
    println(lens(hello))
  }
  // 指定了返回值的方法
  def len(obj: AnyRef):Int = {
    return obj.toString.length
  }
  // 类型推断，自动判断返回类型
  def lens(obj: AnyRef) = {
    obj.toString.length
  }
}