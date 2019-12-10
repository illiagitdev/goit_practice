package com.goit.practice.atomics;

public class Main {
    //Пример использования compareAndSet, но только реализован костомно
    private static CASCounter counter = new CASCounter();
    //Если вы раскомментируете использование велью, вы увидите что у потоков образуется Data race
    private static int value = 0;
    //Volatile переменная нам тоже не поможет, так как волатайл еффективная только если мы делаем атомарные операции
    private static volatile int valueVolatile = 0;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10_000; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(10);
                    counter.increment();
//                    value++;
//                    valueVolatile++;
                } catch (InterruptedException e) {
                    //NOP
                }
            }).start();
        }
        Thread.sleep(2000);
        System.out.println(counter.getValue());
//        System.out.println(value);
//        System.out.println(valueVolatile);
    }
}
