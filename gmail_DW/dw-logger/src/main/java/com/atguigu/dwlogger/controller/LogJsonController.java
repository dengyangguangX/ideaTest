package com.atguigu.dwlogger.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
public class LogJsonController {

    private static final  org.slf4j.Logger logger = LoggerFactory.getLogger(LogJsonController.class);
    @ResponseBody
    @RequestMapping("log")
    public void shipLog(@RequestParam("log") String log){

        JSONObject jsonObject = JSON.parseObject(log);
        jsonObject.put("ts",System.currentTimeMillis());
        String logNew  = jsonObject.toJSONString();
        System.out.println(log);
        logger.info(log);

    }


}
