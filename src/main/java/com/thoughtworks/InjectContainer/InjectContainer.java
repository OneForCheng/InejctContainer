package com.thoughtworks.InjectContainer;

import com.thoughtworks.InjectContainer.exception.InjectException;
import com.thoughtworks.InjectContainer.resolver.QualifierResolver;
import com.thoughtworks.InjectContainer.resolver.ConstructorResolver;
import com.thoughtworks.InjectContainer.resolver.SingletonResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;

public class InjectContainer {
    private final Set<Class<?>> creatingClasses = Collections.synchronizedSet(new HashSet<>());

    private final SingletonResolver singletonResolver = new SingletonResolver();

    private final QualifierResolver qualifierResolver = new QualifierResolver();

    public void registerQualifiedClass(Class<?> clazz) {
        qualifierResolver.registerQualifiedClass(clazz);
    }

    public  <T> T  getInstance(Class<T> clazz) {
        Object instance = singletonResolver.getSingletonOrNull(clazz);

        if (instance == null) {
            instance = createFromClass(clazz);
            singletonResolver.tryRegisterSingleton(clazz, instance);
        }

        return (T)instance;
    }

    private <T> T createFromClass(Class<T> clazz) {
        creatingClasses.add(clazz);

        Constructor<T> constructor = ConstructorResolver.getUniqueInjectableConstructor(clazz);
        T instance = createFromConstructor(constructor);

        creatingClasses.remove(clazz);

        return instance;
    }

    private <T> T createFromConstructor(Constructor<T> constructor) {
        Object[] params = Arrays.stream(constructor.getParameters()).map(param -> createFromParameter(constructor, param)).toArray();
        try {
            return constructor.newInstance(params);
        } catch (Exception e) {
            throw  new InjectException(String.format("create instance error from %s constructor", constructor.getDeclaringClass().getSimpleName()), e);
        }
    }

    private Object createFromParameter(Constructor<?> constructor, Parameter parameter) {
        Class<?> clazz = qualifierResolver.getUniqueRegisteredQualifiedClassOrNull(parameter);

        if (clazz == null) clazz = parameter.getType();

        if (creatingClasses.contains(clazz)) {
            throw new InjectException(String.format("circular dependency on constructor, the class is %s", constructor.getDeclaringClass().getSimpleName()));
        }

        return getInstance(clazz);
    }
}
