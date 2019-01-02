package com.atguigu.sparkmall.mock


import scala.util.Random

object RandomNum {

//  def main(args: Array[String]): Unit = {
//    for(i<- 0 to 100) {
//      println(RandomNum.multi(1, 5, RandomNum(1, 5), ",", false))
//    }
//    val stringBuilder = new StringBuilder()
//    val bool: Boolean = stringBuilder.append(1).contains("1".charAt(0))
//    println('1'.equals(1.toChar))
//    println(bool)
//  }

  def apply(fromNum:Int,toNum:Int): Int =  {
    fromNum+ new Random().nextInt(toNum-fromNum+1)
  }
  def multi(fromNum:Int,toNum:Int,amount:Int,delimiter:String,canRepeat:Boolean) ={
    // 实现方法  在fromNum和 toNum之间的 多个数组拼接的字符串 共amount个
    //用delimiter分割  canRepeat为false则不允许重复
    val stringBuilder = new StringBuilder()
    var index = 1
    while(index <= amount){
      val randoms = apply(fromNum,toNum)
      if(canRepeat || !stringBuilder.contains(randoms.toString.charAt(0))) {
        stringBuilder.append(randoms)
        if(index != amount){
          stringBuilder.append(delimiter)
        }
        index += 1
      }
    }
    stringBuilder.toString()

  }


}
