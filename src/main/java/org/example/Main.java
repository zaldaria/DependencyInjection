package org.example;
/**
 * Главный класс приложения, демонстрирующий работу механизма внедрения зависимостей.
*/
 public class Main {
    public static void main(String[] args) {
        SomeBean sb = (new Injector()).inject(new SomeBean());
        sb.foo();
    }
}


