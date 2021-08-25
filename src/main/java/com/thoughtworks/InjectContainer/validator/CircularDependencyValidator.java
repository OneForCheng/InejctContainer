package com.thoughtworks.InjectContainer.validator;

import com.thoughtworks.InjectContainer.exception.InjectException;

import java.util.*;

public class CircularDependencyValidator {
    private final Set<Class<?>> creatingClasses = Collections.synchronizedSet(new HashSet<>());

    public void entry(Class<?> clazz) {
        if (creatingClasses.contains(clazz)) {
            throw new InjectException(String.format("circular dependency on constructor for injection class %s", clazz.getSimpleName()));
        }
        creatingClasses.add(clazz);
    }

    public void exit(Class<?> clazz) {
        creatingClasses.remove(clazz);
    }
}
