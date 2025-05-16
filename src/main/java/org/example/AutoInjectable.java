package org.example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для пометки полей, которые должны быть автоматически заполнены
 * зависимостями
 * <p>
 * Поля, помеченные этой аннотацией, будут автоматически инициализированы
 * соответствующими реализациями, указанными в файле config.properties
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoInjectable {

}