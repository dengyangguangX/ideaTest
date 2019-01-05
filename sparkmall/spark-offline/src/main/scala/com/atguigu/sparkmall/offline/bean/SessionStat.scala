package com.atguigu.sparkmall.offline.bean

case class SessionStat(taskId : String,
                       contiditions : Condition,
                       session_count : Int,
                       session_visitLength_le_10s_ratio : Float,
                       session_visitLength_gt_10s_ratio : Float,
                       session_stepLength_le_5_ratio : Float,
                       session_stepLength_gt_5_ratio : Float
                      )
