package com.goit.practice.atomics;

public class SimpleAtomic {
    private int value;

    public synchronized int getValue() {
        return value;
    }

    public synchronized boolean compareAndSwap(int expectedValue, int newValue) {
        boolean isSwapped = false;
        if (value == expectedValue) {
            value = newValue;
            isSwapped = true;
        }
        return isSwapped;
    }
}