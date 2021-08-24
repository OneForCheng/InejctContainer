package com.thoughtworks.InjectContainer.resolver;

import com.thoughtworks.InjectContainer.annotation.Inject;

import com.thoughtworks.InjectContainer.exception.InjectException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConstructorResolver {
    private static List<Constructor<?>> getInjectableConstructors(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        return Arrays.stream(constructors)
                .filter(constructor -> Modifier.isPublic(constructor.getModifiers()))
                .filter(constructor -> constructor.getParameterCount() == 0 || constructor.isAnnotationPresent(Inject.class))
                .collect(Collectors.toList());
    }

    public static <T> Constructor<T> getUniqueInjectableConstructor(Class<T> clazz) {
        List<Constructor<?>> injectableConstructors = getInjectableConstructors(clazz);

        int size = injectableConstructors.size();
        if (size == 0) {
            throw new InjectException(String.format("no accessible constructor for injection class %s", clazz.getSimpleName()));
        }
        if (size > 1) {
            throw new InjectException(String.format("multiple injectable constructor for injection class %s", clazz.getSimpleName()));
        }

        return  (Constructor<T>)injectableConstructors.get(0);
    }
}
