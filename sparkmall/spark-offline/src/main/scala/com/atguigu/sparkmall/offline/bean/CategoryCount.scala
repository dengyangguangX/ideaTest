package com.atguigu.sparkmall.offline.bean

case class CategoryCount (
                         taskId : String,
                         category_id : Long,
                         click_count : Long,
                         order_count : Long,
                         pay_count : Long
                         )
