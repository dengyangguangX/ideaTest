package com.atguigu.dw.mock.util;

import java.util.Date;
import java.util.Random;

public class RandomDate {

    Long logDateTime =0L;//
    int maxTimeStep=0 ;

    public RandomDate() {
    }

    public RandomDate(Date startDate, Date endDate, int num){
        long avgStepTime = (endDate.getTime() - startDate.getTime()) / num;
        this.maxTimeStep = (int)avgStepTime*2;
        logDateTime = startDate.getTime();
    }

    public Date getRandomDate(){
        int timeStep = new Random().nextInt(maxTimeStep);
        logDateTime += timeStep;
        return new Date(logDateTime);
    }
}
