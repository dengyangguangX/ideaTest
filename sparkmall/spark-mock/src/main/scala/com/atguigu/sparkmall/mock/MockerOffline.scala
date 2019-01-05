package com.atguigu.sparkmall.mock

import java.text.SimpleDateFormat
import java.util.{Date, UUID}

import com.atguigu.sparkmall.common.util.ConfigUtil
import com.atguigu.sparkmall.common.model.DataModel.{CityInfo, ProductInfo, UserInfo, UserVisitAction}
import org.apache.commons.configuration2.FileBasedConfiguration
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object MockerOffline {

  val userNum = 100
  val productNum = 100
  val sessionNum = 10000

  val pageNum = 50
  val cargoryNum = 20


  val logAboutNum = 100000 //日志大致数量，用于分布时间

  val professionRandomOpt = RandomOptions(RanOpt("学生", 40), RanOpt("程序员", 30), RanOpt("经理", 20), RanOpt("老师", 10))

  val genderRandomOpt = RandomOptions(RanOpt("男", 60), RanOpt("女", 40))
  val ageFrom = 10
  val ageTo = 59

  val productExRandomOpt = RandomOptions(RanOpt("自营", 70), RanOpt("第三方", 30))

  val searchKeywordsOptions = RandomOptions(RanOpt("手机", 30), RanOpt("笔记本", 70), RanOpt("内存", 70), RanOpt("i7", 70), RanOpt("苹果", 70), RanOpt("吃鸡", 70))
  val actionsOptions = RandomOptions(RanOpt("search", 20), RanOpt("click", 60), RanOpt("order", 6), RanOpt("pay", 4), RanOpt("quit", 10))


  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setAppName("Mock").setMaster("local[*]")
    val sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()

    // 模拟数据
    val userVisitActionData = this.mockUserAction()
    val userInfoData = this.mockUserInfo()
    val productInfoData = this.mockProductInfo()
    val cityInfoData = this.mockCityInfo()

    // 将模拟数据装换为RDD
    val userVisitActionRdd = sparkSession.sparkContext.makeRDD(userVisitActionData)
    val userInfoRdd = sparkSession.sparkContext.makeRDD(userInfoData)
    val productInfoRdd = sparkSession.sparkContext.makeRDD(productInfoData)
    val cityInfoRdd = sparkSession.sparkContext.makeRDD(cityInfoData)

    import sparkSession.implicits._
    val userVisitActionDF = userVisitActionRdd.toDF()
    val userInfoDF = userInfoRdd.toDF()
    val productInfoDF = productInfoRdd.toDF()
    val cityInfoDF = cityInfoRdd.toDF()


    insertHive(sparkSession, "user_visit_action", userVisitActionDF)
    insertHive(sparkSession, "user_info", userInfoDF)
    insertHive(sparkSession, "product_info", productInfoDF)
    insertHive(sparkSession, "city_info", cityInfoDF)

    sparkSession.close()
  }

  def insertHive(sparkSession: SparkSession, tableName: String, dataFrame: DataFrame): Unit = {
    val config: FileBasedConfiguration = ConfigUtil("config.properties").config
    val databaseName: String = config.getString("hive.database")
    sparkSession.sql("use " + databaseName)
    sparkSession.sql("drop table if exists " + tableName)
    dataFrame.write.saveAsTable(tableName)
    println("保存：" + tableName + "完成")
    sparkSession.sql("select * from " + tableName).show(100)

  }


  def mockUserInfo() = {


    val rows = new ListBuffer[UserInfo]()

    for (i <- 1 to userNum) {
      val user = UserInfo(i,
        "user_" + i,
        "name_" + i,
        RandomNum(ageFrom, ageTo), //年龄
        professionRandomOpt.getRandomOpt(),
        genderRandomOpt.getRandomOpt()
      )
      rows += user
    }
    rows.toList
  }

  def mockUserAction() = {


    val rows = new ListBuffer[UserVisitAction]()

    val startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2018-11-26")
    val endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2018-11-27")
    //val randomDate = RandomDate(startDate, endDate, logAboutNum)
    val listBuffer = new ListBuffer[(String, Int, Long,Int,Long)]
    for (i <- 1 to sessionNum) {
      //List(Map(sessionId,(userId,dateTime)))
      //Map(sessionId->(userId,dateTime))
      val userId = RandomNum(1, userNum)
      val sessionId = UUID.randomUUID().toString
      listBuffer += {(sessionId, userId, RandomDate.getRandomDateTime(startDate,endDate).getTime,0,RandomNum(1, 26).toLong)}
    }
    var acount = 1


    while (listBuffer.size > 0) {
      val randomListNum = RandomNum(0, listBuffer.size - 1)
      var action = actionsOptions.getRandomOpt()
      val randomElem = listBuffer(randomListNum)

      val actionDateTime = randomElem._3 + acount
      if(acount-randomElem._4>1000*60*30 && randomElem._4 != 0){
        action = "quit"
      }
      acount += RandomNum(30,300)
      listBuffer(randomListNum)= ((randomElem._1,randomElem._2,randomElem._3,acount,randomElem._5))
      val actionDate = new Date(actionDateTime)
      val actionDateString = new SimpleDateFormat("yyyy-MM-dd").format(actionDate)
      val actionDateTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(actionDate)

      var searchKeyword: String = null
      var clickCategoryId: Long = -1L
      var clickProductId: Long = -1L
      var orderCategoryIds: String = null
      var orderProductIds: String = null
      var payCategoryIds: String = null
      var payProductIds: String = null

      //var cityId: Long = RandomNum(1, 26).toLong

      action match {
        case "search" => searchKeyword = searchKeywordsOptions.getRandomOpt()
        case "click" => clickCategoryId = RandomNum(1, cargoryNum)
          clickProductId = RandomNum(1, productNum)
        case "order" => orderCategoryIds = RandomNum.multi(1, cargoryNum, RandomNum(1, 5), ",", false)
          orderProductIds = RandomNum.multi(1, productNum, RandomNum(1, 5), ",", false)
        case "pay" => payCategoryIds = RandomNum.multi(1, cargoryNum, RandomNum(1, 5), ",", false)
          payProductIds = RandomNum.multi(1, productNum, RandomNum(1, 5), ",", false)
        case _ => action = "quit"
      }

      val userVisitAction = UserVisitAction(
        actionDateString,
        randomElem._2.toLong,
        randomElem._1,
        RandomNum(1, pageNum).toLong,
        actionDateTimeString,
        searchKeyword,
        clickCategoryId.toLong,
        clickProductId.toLong,
        orderCategoryIds,
        orderProductIds,
        payCategoryIds,
        payProductIds,
        randomElem._5
      )
      rows += userVisitAction

      if (action == "quit") {
        listBuffer.remove(randomListNum)
      }
    }


    rows.toList
  }

  def mockProductInfo() = {
    val rows = new ListBuffer[ProductInfo]()
    for (i <- 1 to productNum) {
      val productInfo = ProductInfo(
        i,
        "商品_" + i,
        productExRandomOpt.getRandomOpt()
      )
      rows += productInfo
    }
    rows.toList
  }


  def mockCityInfo() = {
    List(CityInfo(1L, "北京", "华北"), CityInfo(2L, "上海", "华东"),
      CityInfo(3L, "深圳", "华南"), CityInfo(4L, "广州", "华南"),
      CityInfo(5L, "武汉", "华中"), CityInfo(6L, "南京", "华东"),
      CityInfo(7L, "天津", "华北"), CityInfo(8L, "成都", "西南"),
      CityInfo(9L, "哈尔滨", "东北"), CityInfo(10L, "大连", "东北"),
      CityInfo(11L, "沈阳", "东北"), CityInfo(12L, "西安", "西北"),
      CityInfo(13L, "长沙", "华中"), CityInfo(14L, "重庆", "西南"),
      CityInfo(15L, "济南", "华东"), CityInfo(16L, "石家庄", "华北"),
      CityInfo(17L, "银川", "西北"), CityInfo(18L, "杭州", "华东"),
      CityInfo(19L, "保定", "华北"), CityInfo(20L, "福州", "华南"),
      CityInfo(21L, "贵阳", "西南"), CityInfo(22L, "青岛", "华东"),
      CityInfo(23L, "苏州", "华东"), CityInfo(24L, "郑州", "华北"),
      CityInfo(25L, "无锡", "华东"), CityInfo(26L, "厦门", "华南")


    )
  }


}
