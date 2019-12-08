package com.goit.practice.streams;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class StreamSample {

    public static void main(String[] args) {
        /*Просто использование стримов*/

        //Создадим енам и сущность Пользователь, у которого будет роль, его id в системе и имя.
        //инициализируем коллекцию и заполним ее данными
        List<User> users = Arrays.asList(
                new User(1, "Bob Martin", Role.ADMIN),
                new User(2, "Adam Lallana", Role.USER),
                new User(3, "Chris Paul", Role.USER),
                new User(4, "Paul Pogba", Role.USER),
                new User(5, "Phillip Kuzin", Role.GUEST),
                new User(6, "Super Man", Role.ADMIN)
        );

        //Пример того, как мы работали со простой выборкой в java7
        //filtering - фильтруем данные которые нам нужны, к примеру только Админ
        List<User> filtered = new ArrayList<>();
        for (User user : users) {
            if (user.getRole().equals(Role.ADMIN)) {
                filtered.add(user);
            }
        }

        //sorting - а тут мы сортируем выбранные результаты по какому то критерию, к примеру по длине имени
        Collections.sort(filtered, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return Integer.compare(o1.getName().length(), o2.getName().length());
            }
        });

        //map - функция высшего порядка которая значит - "применить ко всем"
        //соберем все отсартированные именя в список;
        List<String> names = new ArrayList<>();
        for (User user : filtered) {
            names.add(user.getName());
        }

        //Слишком много операций...
        // Попробуем сделать тоже самое но уже с использованием java8 и Stream API

        List<String> usersWithJava8 = users.stream()
                //фильтруем -- не финальная
                .filter(user -> user.getRole().equals(Role.ADMIN))
                //сортируем
                .sorted(Comparator.comparing(User::getName).reversed())
                //собираем в новый список только имен
                .map(User::getName)
                //создаем список
                .collect(toList());
        //Стало ли удобней?
        System.out.println(names.toString());
        System.out.println(usersWithJava8.toString());


        /*----------Промежуточные и терминальные операции------------*/

        //к примеру у нас есть какой то список integer
        List<String> phases = new LinkedList<>();
        Collection<Integer> primes = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 10);

        List<Integer> result = primes.stream()
                // "промежуточные операции" ничего не делают -
                // мы только "билдим query", можно сказать они "lazy"
                // их порядок важен, от этого зависит что будет в результате
                .filter(n -> {
                    phases.add("f-" + n);
                    return n % 2 == 0;
                })
                .map(n -> {
                    phases.add("m-" + n);
                    return n * n;
                })
                //
//                 .sorted((n1, n2) -> {
//                    phases.add("s-" + n1 + "-" + n2);
//                    return Integer.compare(n2, n1);
//                })
                // некотоыре функции влияют на выполнение других
//                .limit(2)
                // и только "терминальная операция" - приводит к вычислениям
                .collect(toList());
        boolean equal = (result.toString() + phases.toString()).equals("[f-1, f-2, m-2, f-3, f-4, m-4, f-5, f-6, m-6, f-7, f-8, m-8, f-10, m-10][4, 16, 36, 64, 100]");
        System.out.println("Is lists is equal " + equal);

        // а теперь расскомментируй .limit(2) и получишь
        // случилось это из за того, что limit - это short-circuiting операция
        // результатом ее выполнения может быть и меньшая коллекция
        // а значит не надо выполянть все предварительные вычисления
        equal = ("[4, 16][f-1, f-2, m-2, f-3, f-4, m-4]").equals(result.toString() + phases.toString());
        System.out.println("Is lists is equal with adding limit? " + equal);

        // добавь sorted и получишь
        // обрати внимание что идет фильтрация и мэппинг всех элементов снова
        // это случилось из за того, что sorting - это statefull операция
        equal = ("[100, 64][f-1, f-2, m-2, f-3, f-4, m-4, f-5, f-6, m-6, f-7, f-8, m-8, f-10, m-10, s-16-4, s-36-16, s-64-36, s-100-64]").equals(result.toString() + phases.toString());
        System.out.println("Is lists is equal with adding sorting? " + equal);

        // если теперь мы заменим users.stream() на users.parallelStream()
        // то угадать исход вычислений мы не сможем - они будут выполняться
        // параллельно на всех ядрах процессора

    }


    enum Role {
        ADMIN("admin"),
        USER("user"),
        GUEST("guest");

        private String role;

        Role(String role) {
            this.role = role;
        }

        public String getRole() {
            return role;
        }
    }

    static class User {
        private long id;
        private String name;
        private Role role;

        public User(long id, String name, Role role) {
            this.id = id;
            this.name = name;
            this.role = role;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Role getRole() {
            return role;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", role=" + role +
                    '}';
        }
    }
}
