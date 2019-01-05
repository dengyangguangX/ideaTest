package com.atguigu.sparkmall.offline.app

import java.text.SimpleDateFormat
import java.util.{Date, UUID}

import com.atguigu.sparkmall.common.model.DataModel.UserVisitAction
import com.atguigu.sparkmall.common.util.{MyAccumulator, RandomObjectT}
import com.atguigu.sparkmall.offline.bean.{Condition, SessionInfo}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}

import scala.util.Random

object SessionExtractorApp {

  def extractSession(sparkSession : SparkSession, conditionConfig : Condition) = {
    sparkSession.sql("use sparkmall0808")
    var sql = "select a.* \nfrom user_visit_action a\nJOIN user_info u\nON a.user_id = u.user_id\nwhere 1 = 1 ";
    sql = CreateSql.createSql(sql,conditionConfig)
    val df: DataFrame = sparkSession.sql(sql)


    val taskId = UUID.randomUUID().toString


    import sparkSession.implicits._
    val ds: Dataset[UserVisitAction] = df.as[UserVisitAction]
    val rdd: RDD[UserVisitAction] = ds.rdd
    //注册累加变量
    val accumulator = new MyAccumulator()
    sparkSession.sparkContext.register(accumulator)

    val sessionAndUserVisitAction: RDD[(String, Iterable[UserVisitAction])] = rdd.map(x=>{(x.session_id,x)}).groupByKey()
    val sessionInfoRdd: RDD[SessionInfo] = sessionAndUserVisitAction.map { case (sessionId, userVisit) =>

      val tuple: (Long, Int, Long, String, String, String, String) = userVisit.foldLeft((Long.MaxValue, 0, 0L, "", "", "", ""))((t, user) => {
        val userDateTime: Long = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(user.action_time).getTime
        val startTime = if (userDateTime < t._1) userDateTime else t._1
        val stepLength = t._2 + 1
        val maxTime = if (userDateTime > t._3) userDateTime else t._3
        val searchKeywords = if (user.search_keyword != null && user.search_keyword != "") t._4 + user.search_keyword else t._4
        val clickProductIds = if (user.click_product_id != -1) t._5 + user.click_product_id else t._5
        val orderProductIds = if (user.order_product_ids != null && user.order_product_ids != "") t._6 + user.order_product_ids else t._6
        val payProductIds = if (user.pay_product_ids != null && user.pay_product_ids != "") t._7 + user.pay_product_ids else t._7
        (startTime, stepLength, maxTime, searchKeywords, clickProductIds, orderProductIds, payProductIds)
      })
      SessionInfo(taskId,
        sessionId,
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(tuple._1)),
        tuple._2,
        ((tuple._3 - tuple._1) / 1000).toInt,
        tuple._4,
        tuple._5,
        tuple._6,
        tuple._7)
    }
    sessionInfoRdd.foreach(x=>println("fff"+x.startTime))
    val timeAndSessionInfo: RDD[(String, SessionInfo)] = sessionInfoRdd.map(x=>{(x.startTime.split(":")(0),x)})
    val timeAndIterable: RDD[(String, Iterable[SessionInfo])] = timeAndSessionInfo.groupByKey()
    val count: Long = sessionInfoRdd.count()
    println("-------------"+count)

    val result: RDD[SessionInfo] = timeAndIterable.flatMap { case (time, sessionIterable) =>
      val list: List[SessionInfo] = sessionIterable.toList

      //      //搜索过的关键词
      //      list.map(x=>(x.session_id,x))
      val size: Int = list.size
      //点击下过的商品
      val extractNum: Long = size * 100 / count
      val index: Int = new Random().nextInt(size)
      val actions: List[SessionInfo] = RandomObjectT.multiList(extractNum, false, list)
      actions
    }
//    result.toDF().write
//      .format("jdbc")
//      .option("url", "jdbc:mysql://hadoop110:3306/sparkmall0808")
//      .option("dbtable", "random_session_info")
//      .option("user", "root")
//      .option("password", "root")
//      .mode(SaveMode.Append)
//      .save()
    result

    //JdbcUtil.executeBatchUpdate("insert into random_session_info values (?,?,?,?,?,?,?) ",Iterable(infoes))


  }

}
