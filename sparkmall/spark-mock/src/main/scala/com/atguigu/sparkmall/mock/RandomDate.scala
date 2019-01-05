package com.atguigu.sparkmall.mock

import java.util.Date

import scala.util.Random

object RandomDate {
  def apply(startDate:Date,endDate:Date,step:Int): RandomDate ={
    val randomDate = new RandomDate()
    val avgStepTime = (endDate.getTime- startDate.getTime)/step
    randomDate.maxTimeStep=avgStepTime*2
    randomDate.lastDateTime=startDate.getTime
    randomDate
  }
  def getRandomDateTime(startDate:Date,endDate:Date):Date = {
    val randomTime = startDate.getTime + new Random().nextInt((endDate.getTime - startDate.getTime).toInt)
    new Date(randomTime)
  }
}

class RandomDate{
  var lastDateTime =0L
  var maxTimeStep=0L

  def  getRandomDate()={
    val timeStep = new Random().nextInt(maxTimeStep.toInt)
    lastDateTime = lastDateTime+timeStep

    new Date( lastDateTime)
  }

}

