package org.example;

public class Main {
    public static void main(String[] args) {
        SomeBean sb = (new Injector()).inject(new SomeBean());
        sb.foo(); // Выведет "AC" или "BC" в зависимости от конфигурации
    }
}


