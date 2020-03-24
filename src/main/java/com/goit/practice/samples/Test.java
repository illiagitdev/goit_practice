package com.goit.practice.samples;

class Test {
    class A {}

    class B extends A { }

    abstract class C {
        abstract void doAction(A a);
        void start(A a){
            doAction(a);
        }
    }

    class D extends C {

        @Override
        void doAction(A a) {
            System.out.println("A action!");
        }

        void doAction(B b) {
            System.out.println("B action!");
        }
    }
    public static void main(String[] args) {

        new Test().new D().start(new Test().new B());
    }
}
