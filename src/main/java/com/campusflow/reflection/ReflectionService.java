package com.campusflow.reflection;

import com.campusflow.annotation.Auditable;
import java.lang.reflect.Method;

public class ReflectionService {

    public String inspect(Class<?> type) {
        StringBuilder result = new StringBuilder();
        result.append("CLASS: ").append(type.getSimpleName()).append('\n');
        result.append("METHODS\n");

        for (Method method : type.getDeclaredMethods()) {
            result.append("- ").append(method.getName());

            if (method.isAnnotationPresent(Auditable.class)) {
                result.append(" [@Auditable: ")
                        .append(method.getAnnotation(Auditable.class).value())
                        .append("]");
            }

            result.append('\n');
        }

        return result.toString();
    }
}
