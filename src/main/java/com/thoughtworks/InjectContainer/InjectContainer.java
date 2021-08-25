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
            creatingClasses.add(clazz);

            instance = createFromClass(clazz);

            creatingClasses.remove(clazz);

            singletonResolver.tryRegisterSingleton(clazz, instance);
        }

        return (T)instance;
    }

    private <T> T createFromClass(Class<T> clazz) {
        Constructor<T> constructor = ConstructorResolver.getUniqueInjectableConstructor(clazz);
        return createFromConstructor(constructor);
    }

    private <T> T createFromConstructor(Constructor<T> constructor) {
        Object[] params = getConstructorParameters(constructor);
        try {
            return constructor.newInstance(params);
        } catch (Exception e) {
            throw  new InjectException(String.format("create instance error from %s constructor", constructor.getDeclaringClass().getSimpleName()), e);
        }
    }

    private <T> Object[] getConstructorParameters(Constructor<T> constructor) {
        Object[] params = Arrays.stream(constructor.getParameters()).map(param -> {
            if (creatingClasses.contains(param.getType())) {
                throw new InjectException(String.format("circular dependency on constructor, the class is %s", constructor.getDeclaringClass().getSimpleName()));
            }
            return createFromParameter(param);
        }).toArray();
        return params;
    }

    private Object createFromParameter(Parameter parameter) {
        Class<?> clazz = qualifierResolver.getUniqueRegisteredQualifiedClassOrNull(parameter);

        if (clazz == null) clazz = parameter.getType();

        return getInstance(clazz);
    }
}
