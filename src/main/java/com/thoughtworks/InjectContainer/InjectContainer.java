package com.thoughtworks.InjectContainer;

import com.thoughtworks.InjectContainer.exceptions.InjectException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InjectContainer {
    private List<Constructor<?>> getInjectableConstructors(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        return Arrays.stream(constructors)
                .filter(constructor -> Modifier.isPublic(constructor.getModifiers()))
                .filter(constructor -> constructor.getParameterCount() == 0 || constructor.isAnnotationPresent(Inject.class))
                .collect(Collectors.toList());
    }

    public  <T> T  getInstance(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        List<Constructor<?>> injectableConstructors = getInjectableConstructors(clazz);

        if (injectableConstructors.isEmpty()) {
            throw new InjectException(String.format("no accessible constructor for injection class %s", clazz.getSimpleName()));
        }

        return clazz.newInstance();
    }
}
