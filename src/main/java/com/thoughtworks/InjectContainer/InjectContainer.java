package com.thoughtworks.InjectContainer;

import com.thoughtworks.InjectContainer.exceptions.InjectException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class InjectContainer {

    private final Set<Class<?>> creatingClasses = Collections.synchronizedSet(new HashSet<>());

    private List<Constructor<?>> getInjectableConstructors(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        return Arrays.stream(constructors)
                .filter(constructor -> Modifier.isPublic(constructor.getModifiers()))
                .filter(constructor -> constructor.getParameterCount() == 0 || constructor.isAnnotationPresent(Inject.class))
                .collect(Collectors.toList());
    }

    public  <T> T  getInstance(Class<T> clazz) {
        List<Constructor<?>> injectableConstructors = getInjectableConstructors(clazz);

        int size = injectableConstructors.size();
        if (size == 0) {
            throw new InjectException(String.format("no accessible constructor for injection class %s", clazz.getSimpleName()));
        }
        if (size > 1) {
            throw new InjectException(String.format("multiple injectable constructor for injection class %s", clazz.getSimpleName()));
        }

        Constructor<T> constructor = (Constructor<T>)injectableConstructors.get(0);

        creatingClasses.add(clazz);

        T target = createFromConstructor(constructor);

        creatingClasses.remove(clazz);

        return target;
    }

    private <T> T createFromConstructor(Constructor<T> constructor) {
        Object[] params = Arrays.stream(constructor.getParameters()).map(param -> {
            if (creatingClasses.contains(param.getType())) {
                throw new InjectException(String.format("circular dependency on constructor, the class is %s", constructor.getDeclaringClass().getSimpleName()));
            }
            return createFromParameter(param);
        }).toArray();
        try {
            return constructor.newInstance(params);
        } catch (Exception e) {
            throw  new InjectException(String.format("create instance error from %s constructor", constructor.getDeclaringClass().getSimpleName()), e);
        }
    }

    private Object createFromParameter(Parameter parameter) {
        Class<?> clazz = parameter.getType();
        return getInstance(clazz);
    }
}
