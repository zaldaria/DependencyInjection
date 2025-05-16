package org.example;

import java.lang.reflect.Field;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class Injector {
    private final Properties properties;

    public Injector() {
        properties = new Properties();
        try {
            // Загружаем файл свойств
            properties.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file", e);
        }
    }

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