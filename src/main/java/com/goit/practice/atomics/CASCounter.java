package com.goit.practice.atomics;
//Compare and Swap counter. Good artice -> https://howtodoinjava.com/java/multi-threading/compare-and-swap-cas-algorithm/
public class CASCounter {
    private SimpleAtomic object = new SimpleAtomic();

    public int getValue() {
        return object.getValue();
    }

    public void increment() {
        int oldValue = object.getValue();
        while (!object.compareAndSwap(oldValue, oldValue + 1)) {
            oldValue = object.getValue();
        }
    }
}
