package com.goit.practice.lambdas;

import java.util.function.Consumer;

public class LambdaCallbackSample {
    public static void main(String[] args) {
        // Попробуем рассмотреть другой пример на основе callback шаблона
        //Button class - это кнопка, на которую можно нажать.
        //Просмотрите все созданныйе ниже классы Button, ActionEvent и интферйес OnClickListener.

        //Приступаем к реализации нашей кнопки. Как бы мы это сделали до Джавы 8??
        Button button = new Button();
        //устанавливаем обработчик на событие click
        button.setOnClick(new OnClickListener() {
            @Override
            public void handle(ActionEvent event) {
                //этот метод отработает только при вызове click метода, очень важный момент
                String log = "on-" + event.getName();
                System.out.println(log);
            }
        });
        //Кто то в в другом конце мира нажимает кнопку клик, и мы получим вывод данного клика.
        button.click();


        //А так бы мы сделали это в Джава 8!
        Button buttonWithLambda = new Button();

        //меньше кода, лучше читабильность
        // мы не думаем про метод, а только анализируем бизнес логику.
        button.setOnClick((event) -> {
            String log = "on-" + event.getName();
            System.out.println(log);
        });

        button.click();

        //Как бы выглядел просто вывод нашего события
        button.setOnClick(System.out::println);
        //Click и отображается информация по нашему переопределенному методу toString
        button.click();

        // приведение лямбды к определенному типу функционального интерфейса
        // произойдет автоматически во время компиляции,
        // а тип этого интерфейса будет взять из контекста.

        //А теперь попробуем использовать уже существующие функциональные интерфейсы для нашей реализации
        //Создадим класс ButtonJava8, ознакомтесь с ним, он статический в этом же классе.
        ButtonJava8 buttonJava8 = new ButtonJava8();
        Consumer<ActionEvent> consumer = (event) -> System.out.println(event);
        buttonJava8.setOnClick(consumer);
        buttonJava8.click();
    }

    static class Button {
        private OnClickListener onClick;

        //OnClickListener -> это клиент который может подписаться на нашу кнопку. Listener выступает тут как callback-функция
        public void setOnClick(OnClickListener onClick) {
            this.onClick = onClick;
        }

        // когда произойдет событие click эта функция будет вызвана
        public void click() {
            if (onClick != null) {
                onClick.handle(new ActionEvent("click"));
            }
        }
    }

    //Consumer идеально подойдет для наших задач. Его метод accept заменит наш метод handle.
    //Всегда ищите уже существующее решение, не нужно создавать свой функциональный интерфейс если
    //в SDK уже есть дефолтные реализации!! Не создавайте велосипеды:)
    static class ButtonJava8 {
        private Consumer<ActionEvent> onCLick;

        public void click() {
            if (onCLick != null) {
                onCLick.accept(new ActionEvent("click"));
            }
        }

        public void setOnClick(Consumer<ActionEvent> consumer) {
            this.onCLick = consumer;
        }
    }

    // а этот объект передается внутрь callback-функции
    // и сообщает ей какой конкретно click произошел
    static class ActionEvent {
        private String name;

        public ActionEvent(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return String.format("ActionEvent name = %s", name);
        }
    }

    // это наш интерфейс, в терминологии джава 8 он называется функциональным.
    //http://cr.openjdk.java.net/~dlsmith/jsr335-0.9.3/A.html
    //или другими словами, любой интерфейс содержащий только 1 метод может быть функциональным
    @FunctionalInterface
    interface OnClickListener {
        void handle(ActionEvent event);
    }

    //Хорошая практика ставить аннотацию @FunctionalInterface, как вы знаете интерфейс
    // с одним абстрактным методом и так будет функциональным, но наличие аннотации в момент компиляции
    // предотвратит желание дописывания других абстрактных методов в наш функциональный интерфейс.
    // попробуйте разкомментировать метод2:)
    @FunctionalInterface
    interface NonFunctionalInterface {
        void method1();
//        void method2();
    }
}
