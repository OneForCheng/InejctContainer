package com.thoughtworks.InjectContainer;

import com.thoughtworks.InjectContainer.exceptions.InjectException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InjectContainer {
    public  <T> T  getInstance(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        List<Constructor<?>> publicConstructors = Arrays.stream(constructors).filter(constructor -> Modifier.isPublic(constructor.getModifiers())).collect(Collectors.toList());
        if (publicConstructors.isEmpty()) {
            throw new InjectException(String.format("no accessible constructor for injection class %s", clazz.getSimpleName()));
        }
        return clazz.newInstance();
    }
}
