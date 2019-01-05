package com.atguigu.sparkmall.offline.app

import java.text.SimpleDateFormat
import java.util.UUID

import com.atguigu.sparkmall.offline.bean.{Condition, SessionStat}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

object SessionStatApp {

  def sessionRatio(sparkSession : SparkSession, conditionConfig : Condition) = {
    var sql = "select session_id, action_time\nfrom user_visit_action a\nJOIN user_info u\nON a.user_id = u.user_id\nwhere 1 = 1 ";
    sql = CreateSql.createSql(sql,conditionConfig)
    val taskId = UUID.randomUUID().toString
    sparkSession.sql("use sparkmall0808")
    val df: DataFrame = sparkSession.sql(sql)
    val rdd: RDD[Row] = df.rdd
    val rdd1: RDD[(String, String)] = rdd.map(x=>(x.getString(0),x.getString(1)))
    val sessionAndTimes: RDD[(String, Iterable[String])] = rdd1.groupByKey()
    val sessionAndTimeAndStep: RDD[(String, Long, Int)] = sessionAndTimes.map { case (session, timesIterable) => {
      //var minTime
      val list: List[String] = timesIterable.toList
      val sizeStep: Int = list.size
      val tuple: (Long, Long) = list.foldLeft((Long.MaxValue, 0L))((t, x) => {
        val time: Long = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(x).getTime
        var minTime: Long = t._1
        var maxTime: Long = t._2
        if (t._1 > time) minTime = time
        if (t._2 < time) maxTime = time
        (minTime, maxTime)
      })
      (session, tuple._2-tuple._1, sizeStep)
    }
    }
    //sessionAndTimeAndStep.aggregate((0 ,0 ,0))((t:Tuple3[Int,Int,Int],x)=>{(1,1,1)})((u1:Tuple3[Int,Int,Int], u2:Tuple3[Int,Int,Int])=>{})
    val result: (Int, Int, Int) = sessionAndTimeAndStep.aggregate((0, 0, 0))((t, x) => {
      var timeThen10 = t._1
      var stepThen5 = t._2
      var sum = t._3
      if (x._2 > 10) timeThen10 += 1
      if (x._3 > 5) stepThen5 += 1
      sum += 1
      (timeThen10, stepThen5, sum)
    }, (u1, u2) => {
      (u1._1 + u2._1, u1._2 + u2._2, u1._3 + u2._3)
    })
    SessionStat(taskId,
      conditionConfig,
      result._3,
      1-result._1.toFloat/result._3,
      result._1.toFloat/result._3,
      1-result._2.toFloat/result._3,
      result._2.toFloat/result._3
    )
  }

}
