package com.atguigu.sparkmall.offline.bean

case class SessionInfo(taskId : String,
                       sessionId : String,
                       startTime : String,
                       stepLength : Int,
                       visitLength : Int,
                       searchKeywords : String,
                       clickProductIds : String,
                       orderProductIds : String,
                       payProductIds : String
                      )
