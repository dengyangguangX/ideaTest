package com.atguigu.sparkmall.offline.app

import com.atguigu.sparkmall.offline.bean.Condition

import scala.collection.mutable.ListBuffer

object CreateSql {

  def createSql(sql : String, conditionConfig : Condition) : String = {

    var sqlNew = sql
    val conditionList = new ListBuffer[(String,String,Any)]
    conditionList += (
      ("date"," >= ",conditionConfig.startDate),
      ("date", " <= ", conditionConfig.endDate),
      ("age", " >= ", conditionConfig.startAge),
      ("age", " <= ", conditionConfig.endAge),
      ("professionals", " >= ", conditionConfig.professionals),
      ("city_id", " = ", conditionConfig.city),
      ("gender", " = ", conditionConfig.gender),
      ("search_keyword", " in ", conditionConfig.keywords),
      ("order_category_ids", " in ", conditionConfig.categoryIds),
      ("page_id", " in ", conditionConfig.targetPageFlow)
    )

    conditionList.foreach(
      _ match {
        case (str, symbol,condition) => {if(!"".equals(condition) && condition != null) sqlNew += ("and " + str + symbol +"'" + condition + "'")}
      }
    )

    sqlNew
  }

}
