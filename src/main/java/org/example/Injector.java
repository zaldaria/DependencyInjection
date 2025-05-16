package org.example;

import java.lang.reflect.Field;
import java.util.Properties;
import java.io.IOException;

/**
 * Класс для внедрения зависимостей, который автоматически инжектирует реализации
 * в поля, помеченные аннотацией AutoInjectable, на основе конфигурации из файла config.properties
 */
public class Injector {
    private final Properties properties;
    /**
     * Создает новый экземпляр Injector и загружает зависимости
     * из файла config.properties, который должен находится в resources
     *
     * @throws RuntimeException если файл конфигурации не может быть загружен
     */
    public Injector() {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file", e);
        }
    }
    /**
     * Выполняет внедрение зависимостей в переданный объект
     * <p>
     * Сканирует все поля объекта на наличие аннотации AutoInjectable и
     * внедряет соответствующие реализации согласно конфигурации
     * </p>
     *
     * @param <T> тип объекта для внедрения зависимостей
     * @param object целевой объект для внедрения зависимостей
     * @return объект с внедренными зависимостями
     * @throws RuntimeException если внедрение зависимостей не удалось
     */
    public <T> T inject(T object) {
        Class<?> clazz = object.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(AutoInjectable.class)) {
                Class<?> fieldType = field.getType();
                String implementationClassName = properties.getProperty(fieldType.getName());

                if (implementationClassName == null) {
                    throw new RuntimeException("No implementation specified for " + fieldType.getName());
                }

                try {
                    Class<?> implementationClass = Class.forName(implementationClassName.trim());
                    Object implementationInstance = implementationClass.getDeclaredConstructor().newInstance();

                    field.setAccessible(true);
                    field.set(object, implementationInstance);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to inject dependency for field " + field.getName(), e);
                }
            }
        }
        return object;
    }
}