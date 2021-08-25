package com.thoughtworks.InjectContainer.resolver;

import com.thoughtworks.InjectContainer.annotation.Singleton;

import java.util.*;

public class SingletonResolver {
    private final Map<Class<?>, Object> singletonClasses = Collections.synchronizedMap(new HashMap<>());

    public boolean tryRegisterSingleton(Class<?> clazz, Object instance) {
        if (!isSingletonClass(clazz)) return false;
        singletonClasses.put(clazz, instance);
        return true;
    }

    public Object getSingletonOrNull(Class<?> clazz) {
        if (!isSingletonClass(clazz)) return null;
        return singletonClasses.get(clazz);
    }

    private boolean isSingletonClass(Class<?> clazz) {
        return clazz.isAnnotationPresent(Singleton.class);
    }
}
