package com.atguigu.dw.mock.util;

public class RandomOpt<T> {
    private T value ;
    private int weight;

    public RandomOpt ( T value, int weight ){
        this.value=value ;
        this.weight=weight;
    }

    public T getValue() {
        return value;
    }

    public int getWeight() {
        return weight;
    }

}
