package com.goit.practice.lambdas;

public class LambdaExtractSample {
    public static void main(String[] args) {
        //У нас есть сумма всех i реализована с помощью цикла
        testSum_i();

        //Также есть такая же сумма, но от 2*PI*sin((i-1)/100)
        // Можно заметить что в ней такой же цикл
        // как мы можем устранить этот цикл? Это возможно!
        testSum_2();

        //Для этого создадим интерфейс MyFunction
        // у него будет только один get метод
        //Создадим 2 реализации нашей функции.
        MyFunction prime = new MyFunction() {
            public double get(double input) {
                return input;
            }
        };

        MyFunction sin = new MyFunction() {
            public double get(double input) {
                return Math.cos(Math.PI * (input - 1) / 100);
            }
        };

        //Мы вынесли наши формулы, теперь осталось выделить цикл в отдельный метод
        //Таким образом мы смогли сделать универсальную функцию которая будет делать для нас вычисления
        // и проходить по циклу в заданном диапазон
        double primeSum = sum(1, 100, prime);
        System.out.println("prime sum is equals? " + (primeSum == 5050));
        double sinSum = sum(1, 100, sin);
        System.out.println("sin sum is equals? " + sinSum);

        //Мы можем сократить наши инициализации функционального интерфейса
        MyFunction primeShort = input -> input;
        MyFunction sinShort = input -> Math.cos(Math.PI * (input - 1) / 100);
        //И вообще перенести эту логику в вызов метода суммирования
        sum(1, 100, input -> input);
        sum(1, 100, input -> Math.cos(Math.PI * (input - 1) / 100));
    }

    public static void testSum_i() {
        double result = 0;
        for (int i = 1; i <= 100; i++) {
            result += i;
        }
        boolean verifyResult = result == 5050;
        System.out.println("testSum_i - result is equals ? " + verifyResult);
    }

    public static void testSum_2() {
        double result = 0;
        for (int i = 1; i <= 100; i++) {
            result += Math.cos(Math.PI * (i - 1) / 100);
        }
        boolean verifyResult = result == 0.9999999999999929;
        System.out.println("testSum_2 - result is equals ? " + verifyResult);
    }

    public static double sum(int from, int to, MyFunction function) {
        double result = 0;
        for (int i = from; i <= to; i++) {
            result += function.get(i);
        }
        return result;
    }

    @FunctionalInterface
    public interface MyFunction {
        double get(double input);
    }
}
