package com.atguigu.dw.mock.util;

import com.atguigu.dw.mock.util.RandomOpt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomOptGroup<T> {

    private int totalWeight=0;

    private List<RandomOpt<T>> optList=new ArrayList();


    public RandomOptGroup(RandomOpt<T>... opts){
        for (RandomOpt<T> opt : opts) {
            int weight = opt.getWeight();
            totalWeight += weight;
            for (int i = 0; i < weight; i++) {
                optList.add(opt);
            }
        }
    }

    public RandomOpt<T> getRandomOpt(){
        int i = new Random().nextInt(totalWeight);
        return optList.get(i);
    }

}
