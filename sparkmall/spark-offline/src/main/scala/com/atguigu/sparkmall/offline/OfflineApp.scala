package com.atguigu.sparkmall.offline

import java.text.SimpleDateFormat
import java.util.{Date, UUID}

import com.alibaba.fastjson.{JSON, JSONObject}
import com.atguigu.sparkmall.common.util.{ConfigUtil, JdbcUtil, MyAccumulator, RandomObjectT}
import com.atguigu.sparkmall.common.model.DataModel.UserVisitAction
import com.atguigu.sparkmall.offline.app.{CreateSql, SessionExtractorApp}
import com.atguigu.sparkmall.offline.bean.{CategoryCount, Condition, SessionInfo}
import org.apache.commons.configuration2.FileBasedConfiguration
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}

import scala.collection.mutable.ListBuffer
import scala.util.Random

object OfflineApp {

  def main(args: Array[String]): Unit = {
    val sparkSession: SparkSession = SparkSession.builder()
      .enableHiveSupport()
      .master("local[*]")
      .appName("OfflineApp")
      .getOrCreate()

    val config: FileBasedConfiguration = ConfigUtil("condition.properties").config
    val configJsonString: String = config.getString("condition.params.json")
    val jSONObject: JSONObject = JSON.parseObject(configJsonString)

    val conditionConfig = Condition(
      jSONObject.getString("startDate"),
      jSONObject.getString("endDate"),
      jSONObject.getInteger("startAge"),
      jSONObject.getInteger("endAge"),
      jSONObject.getString("professionals"),
      jSONObject.getString("city"),
      jSONObject.getString("gender"),
      jSONObject.getString("keywords"),
      jSONObject.getString("categoryIds"),
      jSONObject.getString("targetPageFlow")
    )


    //val resutl: SessionStat = SessionStatApp.sessionRatio(sparkSession,conditionConfig)

    //SessionExtractorApp.extractSession(sparkSession,conditionConfig)

    //val sessionInfoRdd: RDD[SessionInfo] = SessionExtractorApp.extractSession(sparkSession,conditionConfig)

    sparkSession.sql("use sparkmall0808")
    var sql = "select a.* \nfrom user_visit_action a\nJOIN user_info u\nON a.user_id = u.user_id\nwhere 1 = 1 ";
    sql = CreateSql.createSql(sql,conditionConfig)
    val df: DataFrame = sparkSession.sql(sql)


    val taskId = UUID.randomUUID().toString


    import sparkSession.implicits._
    val ds: Dataset[UserVisitAction] = df.as[UserVisitAction]
    val rdd: RDD[UserVisitAction] = ds.rdd


//    rdd.aggregate((0L, 0L, 0L))((t, x) =>{
//      val clickCount = if (x.click_product_id != -1) t._1 + 1 else t._1
//      val orderCount = if (x.order_product_ids != null && x.order_product_ids != "") t._2 + x.order_product_ids.split(",").size else t._2
//      val c = x.pay_product_ids
//      val payCount = if (x.pay_product_ids != null && x.pay_product_ids != "") t._3 + x.pay_product_ids.split(",").size.toLong else t._3
//      (clickCount, orderCount, payCount)
//    },(u1,u2)=>{})

    val value: RDD[(Long, (String, Int))] = rdd.flatMap(x => {
      val listBuffer: ListBuffer[(Long, (String, Int))] = ListBuffer[(Long,(String, Int))]()
      if (x.click_category_id != -1) {
        listBuffer += (x.click_category_id ->("click",1))
      }
      if (x.order_category_ids != null && x.order_category_ids != "") {
        x.order_category_ids.split(",").foreach(x => {
          listBuffer += (x.toLong -> ("order",1))
        })
      }
      if (x.pay_category_ids != null && x.pay_category_ids != "") {
        x.pay_category_ids.split(",").foreach(x => {
          listBuffer += (x.toLong -> ("pay",1))
        })
      }
      listBuffer
    })
    val vvvv: RDD[CategoryCount] = value.groupByKey().map { case (categoryId, action) =>
      val tuple: (Long, Long, Long) = action.foldLeft((0L, 0L, 0L))((t, x) => {
        val clickCount = if (x._1 == "click") t._1 + 1 else t._1
        val orderCount = if (x._1 == "order") t._2 + 1 else t._2
        val payCount = if (x._1 == "pay") t._3 + 1 else t._3
        (clickCount, orderCount, payCount)
      })
      CategoryCount(taskId, categoryId, tuple._1, tuple._2, tuple._3)
    }

    vvvv.sortBy(x=>(x.click_count,x.order_count),false).collect().foreach(println)

//    val categoryIdAndIter: RDD[(Long, Iterable[UserVisitAction])] = rdd.map(x=>(x.click_category_id,x)).groupByKey()
//    val result: RDD[CategoryCount] = categoryIdAndIter.map { case (categoryId, userIter) =>
//      val a =12
//      val tuple: (Long, Long, Long) = userIter.foldLeft((0L, 0L, 0L))((t, x) => {
//        val clickCount = if (x.click_product_id != -1) t._1 + 1 else t._1
//        val orderCount = if (x.order_product_ids != null && x.order_product_ids != "") t._2 + x.order_product_ids.split(",").size else t._2
//        val c = x.pay_product_ids
//        val payCount = if (x.pay_product_ids != null && x.pay_product_ids != "") t._3 + x.pay_product_ids.split(",").size.toLong else t._3
//        (clickCount, orderCount, payCount)
//      })
//      CategoryCount(taskId, categoryId, tuple._1, tuple._2, tuple._3)
//    }
    //result.foreach(println)
//    val counts: Array[CategoryCount] = result.collect()
//    val c: Array[CategoryCount] = counts.sortWith((c1, c2) => {
//      var flag = false
//      if (c1.click_count > c2.click_count) {
//        flag = true
//      } else if (c1.click_count == c2.click_count) {
//        if (c1.order_count > c2.order_count) {
//          flag = true
//        } else if (c1.order_count == c2.order_count) {
//          if (c1.pay_count > c2.pay_count) {
//            flag = true
//          }
//          //flag = false
//        }
//      }
//      flag
//    })
//    c.foreach(println)





    //    sessionAndTimeAndStep.fold(("0",0L,0))((t, x)=>{
//      var timeThen10 = t._1
//      var stepThen5 = t._2
//      var sum = t._3
//      if(x._2>10) timeThen10 += 1
//      if(x._3>5) stepThen5 += 1
//      sum += 1
//      (timeThen10,stepThen5,sum)
//    })
    //sessionAndTimeAndStep.foreach(println)

  }

}
